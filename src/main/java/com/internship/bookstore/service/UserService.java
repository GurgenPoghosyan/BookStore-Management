package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.RoleNotFoundException;
import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.common.threads.UserSaveThread;
import com.internship.bookstore.common.util.FileSplitter;
import com.internship.bookstore.common.util.UserCsvParser;
import com.internship.bookstore.persistence.entity.CommunityEntity;
import com.internship.bookstore.persistence.entity.RoleEntity;
import com.internship.bookstore.persistence.entity.UserDetailsEntity;
import com.internship.bookstore.persistence.entity.UserEntity;
import com.internship.bookstore.persistence.repository.CommunityRepository;
import com.internship.bookstore.persistence.repository.RoleRepository;
import com.internship.bookstore.persistence.repository.UserDetailsRepository;
import com.internship.bookstore.persistence.repository.UserRepository;
import com.internship.bookstore.service.criteria.UserSearchCriteria;
import com.internship.bookstore.service.dto.CommunityDto;
import com.internship.bookstore.service.dto.RoleDto;
import com.internship.bookstore.service.dto.UserDetailsDto;
import com.internship.bookstore.service.dto.UserDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final CommunityRepository communityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptEncoder;
    private final UserCsvParser userCsvParser;

    public UserDto create(UserDto userDto) {
        if (userDto.getUsername() == null) {
            throw new NullPointerException("Username is required");
        }
        if (userDto.getPassword() == null) {
            throw new NullPointerException("Password is required");
        }
        if (userDto.getDetails() == null) {
            throw new NullPointerException("User details are required");
        }
        if (userDto.getRole() == null) {
            throw new NullPointerException("Role is required");
        }
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
        RoleEntity roleEntity = roleRepository.findByName(userDto.getRole().getName()).orElseThrow(()->new RoleNotFoundException(userDto.getRole().getName()));
        userEntity.setRole(roleEntity);
        List<CommunityDto> userCommunities = userDto.getUserCommunities();
        if (!CollectionUtils.isEmpty(userCommunities)) {
            List<CommunityEntity> communities = userCommunities.stream().map(CommunityDto::mapDtoToEntity).collect(Collectors.toList());
            userEntity.setUserCommunities(communityRepository.saveAll(communities));
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
            userEntity.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
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

    public void saveAll(List<UserEntity> list) {
        userRepository.saveAll(list);
    }

    public void mapFileToListOfEntities(MultipartFile multipartFile) {
            List<UserDto> list = userCsvParser.parseCsv(multipartFile);
            List<UserEntity> users = new ArrayList<>();
            for (UserDto userDto : list) {
                UserEntity userEntity = UserDto.mapDtoToEntity(userDto);
                RoleEntity roleEntity = roleRepository.findById(1L).orElseThrow(()->new RoleNotFoundException(1L));
                userEntity.setRole(roleEntity);

                UserDetailsDto details = userDto.getDetails();
                UserDetailsEntity userDetailsEntity = UserDetailsDto.mapDtoToEntity(details);
                UserDetailsEntity savedDetails = userDetailsRepository.save(userDetailsEntity);
                userEntity.setDetails(savedDetails);

                List<CommunityDto> communities = userDto.getUserCommunities();
                List<CommunityEntity> communityEntities = communities.stream().map(CommunityDto::mapDtoToEntity).collect(Collectors.toList());
                List<CommunityEntity> entities = communityRepository.saveAll(communityEntities);
                userEntity.setUserCommunities(entities);

                users.add(userEntity);
            }
            saveAll(users);
    }

    public void saveUsers(MultipartFile multipartFile){
        ExecutorService es = Executors.newFixedThreadPool(200);
        FileSplitter fileSplitter = new FileSplitter();
        try {
            List<File> files = fileSplitter.splitFile(multipartFile,1);

            for (File file : files) {
                FileItem fileItem = new DiskFileItem(file.getName(), Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

                try {
                     IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                MultipartFile multipartFile1 = new CommonsMultipartFile(fileItem);
                UserSaveThread userSaveThread = new UserSaveThread(this,multipartFile1,userCsvParser);
                userSaveThread.run();
                es.submit(userSaveThread);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }
}
