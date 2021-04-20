package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.RoleNotFoundException;
import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.common.threads.Multithreading;
import com.internship.bookstore.common.threads.UserSaveThread;
import com.internship.bookstore.common.util.CsvParser;
import com.internship.bookstore.common.util.FileSplitter;
import com.internship.bookstore.persistence.entity.*;
import com.internship.bookstore.persistence.repository.CommunityRepository;
import com.internship.bookstore.persistence.repository.RoleRepository;
import com.internship.bookstore.persistence.repository.UserDetailsRepository;
import com.internship.bookstore.persistence.repository.UserRepository;
import com.internship.bookstore.security.session.SessionUser;
import com.internship.bookstore.service.criteria.UserSearchCriteria;
import com.internship.bookstore.service.dto.CommunityDto;
import com.internship.bookstore.service.dto.FileStorageDto;
import com.internship.bookstore.service.dto.UserDetailsDto;
import com.internship.bookstore.service.dto.UserDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final CommunityRepository communityRepository;
    private final FileStorageService fileStorageService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptEncoder;
    private final CsvParser userCsvParser;
    private final Multithreading multithreading;
    @Value("${user.file.split-dir}")
    private String uploadDir;

    public UserDto create(UserDto userDto) {
        UserEntity user = userRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            throw new RuntimeException("Username is already in use");
        }
        UserEntity userEntity = UserDto.mapDtoToEntity(userDto);
        userEntity.setPassword(bCryptEncoder.encode(userDto.getPassword()));
        UserDetailsEntity details = UserDetailsDto.mapDtoToEntity(userDto.getDetails());
        details.setUser(userEntity);
        UserDetailsEntity savedDetails = userDetailsRepository.save(details);
        userEntity.setDetails(savedDetails);
        RoleEntity roleEntity = roleRepository.findByName(userDto.getRole().getName()).orElseThrow(() -> new RoleNotFoundException(userDto.getRole().getName()));
        userEntity.setRole(roleEntity);
        List<CommunityDto> userCommunities = userDto.getUserCommunities();
        for (CommunityDto communityDto : userCommunities) {
            CommunityEntity communityEntity = communityRepository.findByNameAndZip(communityDto.getName(), communityDto.getZip());
            if (communityEntity == null) {
                communityEntity = communityRepository.save(CommunityDto.mapDtoToEntity(communityDto));
            }
            userEntity.getUserCommunities().add(communityEntity);
        }
        UserEntity savedUser = userRepository.save(userEntity);
        return UserDto.mapEntityToDto(savedUser);
    }

    public UserDto getUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserDto.mapEntityToDto(userEntity);
    }

    public QueryResponseWrapper<UserDto> getUsers(UserSearchCriteria criteria) {
        Page<UserEntity> entityContent = userRepository.find(criteria.getUsername(),
                criteria.getStatus(),
                criteria.getFirstName(),
                criteria.getLastName(),
                criteria.getRole(),
                criteria.getZipCode(),
                criteria.composePageRequest());
        Page<UserDto> dtoContent = entityContent.map(UserDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public UserDto update(UserDto userDto, Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (userDto.getUsername() != null) {
            userEntity.setUsername(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            userEntity.setPassword(bCryptEncoder.encode(userDto.getPassword()));
        }
        if (userDto.getStatus() != null) {
            userEntity.setStatus(userDto.getStatus());
        }
        UserEntity updatedUser = userRepository.save(userEntity);
        return UserDto.mapEntityToDto(updatedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public FileStorageDto uploadFile(MultipartFile multipartFile, SessionUser sessionUser) {
        FileStorageEntity fileStorageEntity = new FileStorageEntity();
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + extension;
        fileStorageEntity.setFileName(fileName);
        fileStorageEntity.setExtension(extension);
        FileStorageEntity savedFile = fileStorageService.storeFile(multipartFile, fileStorageEntity);

        UserEntity userEntity = userRepository.findById(sessionUser.getId()).orElseThrow(() -> new UserNotFoundException(sessionUser.getId()));
        userEntity.setUserAccountImage(savedFile);
        userRepository.save(userEntity);
        return FileStorageDto.mapEntityToDto(savedFile);
    }

    public void saveAll(List<UserEntity> list) {
        for (UserEntity userEntity : list) {
            List<CommunityEntity> userCommunities = userEntity.getUserCommunities();
            List<CommunityEntity> communityEntities = communityRepository.saveAll(userCommunities);
            userEntity.setUserCommunities(communityEntities);
            UserDetailsEntity details = userEntity.getDetails();
            UserDetailsEntity savedDetails = userDetailsRepository.save(details);
            userEntity.setDetails(savedDetails);
            RoleEntity roleEntity = roleRepository.findById(1L).orElseThrow(() -> new RoleNotFoundException(1L));
            userEntity.setRole(roleEntity);
        }
        userRepository.saveAll(list);
    }

    public void saveUsers(MultipartFile csvFile) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        String pathname = String.join(File.separator, uploadDir, csvFile.getOriginalFilename());
        File parentCsv = new File(pathname);
        FileSplitter fileSplitter = new FileSplitter();
        parentCsv.mkdirs();
        try {
            csvFile.transferTo(parentCsv);
            List<File> files = fileSplitter.splitFile(parentCsv, 1);
            for (File file : files) {
                UserSaveThread thread = new UserSaveThread(this, userCsvParser, file);
                executorService.submit(thread);
            }
            executorService.shutdown();
//            FileUtils.deleteDirectory(parentCsv.getParentFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
