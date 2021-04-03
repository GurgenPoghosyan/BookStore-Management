package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.persistence.entity.CommunityEntity;
import com.internship.bookstore.persistence.entity.UserDetailsEntity;
import com.internship.bookstore.persistence.entity.UserEntity;
import com.internship.bookstore.persistence.repository.CommunityRepository;
import com.internship.bookstore.persistence.repository.UserDetailsRepository;
import com.internship.bookstore.persistence.repository.UserRepository;
import com.internship.bookstore.service.criteria.UserSearchCriteria;
import com.internship.bookstore.service.dto.CommunityDto;
import com.internship.bookstore.service.dto.UserDetailsDto;
import com.internship.bookstore.service.dto.UserDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
        UserEntity user = userRepository.findbyUserName(userDto.getUsername());
        if (user!=null){
            throw new RuntimeException("Username is already in use");
        }
        UserEntity userEntity = UserDto.mapDtoToEntity(userDto);
        UserDetailsEntity details = UserDetailsDto.mapDtoToEntity(userDto.getDetails());
        details.setUser(userEntity);
        UserDetailsEntity savedDetails = userDetailsRepository.save(details);
        userEntity.setDetails(savedDetails);
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
        if (userDto.getStatus()!=null){
            userEntity.setStatus(userDto.getStatus());
        }
        UserEntity updatedUser = userRepository.save(userEntity);
        return UserDto.mapEntityToDto(updatedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
