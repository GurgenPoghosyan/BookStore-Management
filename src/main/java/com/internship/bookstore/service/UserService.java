package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.persistence.entity.CommunityEntity;
import com.internship.bookstore.persistence.entity.UserDetailsEntity;
import com.internship.bookstore.persistence.entity.UserEntity;
import com.internship.bookstore.persistence.repository.CommunityRepository;
import com.internship.bookstore.persistence.repository.UserDetailsRepository;
import com.internship.bookstore.persistence.repository.UserRepository;
import com.internship.bookstore.service.dto.CommunityDto;
import com.internship.bookstore.service.dto.UserDetailsDto;
import com.internship.bookstore.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserDetailsService userDetailsService;
    private final CommunityService communityService;

    public UserDto create(UserDto userDto) {
        UserEntity userEntity = UserDto.mapDtoToEntity(userDto);
        UserDetailsEntity details = UserDetailsDto.mapDtoToEntity(userDto.getDetails());
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

    //todo how to update user
    public UserDto update(UserDto userDto, Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        userEntity.setStatus(userDto.getStatus());
        UserDetailsDto updatedUserDetails = userDetailsService.update(userDto.getDetails(), userEntity.getDetails().getId());
        userEntity.setDetails(UserDetailsDto.mapDtoToEntity(updatedUserDetails));

        UserEntity updatedUser = userRepository.save(userEntity);
        return UserDto.mapEntityToDto(updatedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
