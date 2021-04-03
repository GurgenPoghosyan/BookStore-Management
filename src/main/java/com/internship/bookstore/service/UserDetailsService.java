package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.UserDetailsNotFoundException;
import com.internship.bookstore.persistence.entity.UserDetailsEntity;
import com.internship.bookstore.persistence.repository.UserDetailsRepository;
import com.internship.bookstore.service.criteria.UserDetailsSearchCriteria;
import com.internship.bookstore.service.dto.UserDetailsDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsDto create(UserDetailsDto userDetailsDto) {
        if (userDetailsDto.getFirstName() == null) {
            throw new NullPointerException("FirstName is required");
        }
        if (userDetailsDto.getLastName() == null) {
            throw new NullPointerException("LastName is required");
        }
        if (userDetailsDto.getEmailAddress() == null) {
            throw new NullPointerException("Email address is required");
        }
        UserDetailsEntity detailsEntity = userDetailsRepository.findByEmailAddress(userDetailsDto.getEmailAddress());
        if (detailsEntity !=null){
            throw new RuntimeException("Email address is already in use");
        }
        UserDetailsEntity entity = UserDetailsDto.mapDtoToEntity(userDetailsDto);
        UserDetailsEntity savedUserDetails = userDetailsRepository.save(entity);
        return UserDetailsDto.mapEntityToDto(savedUserDetails);
    }

    public UserDetailsDto getUserDetail(Long id) {
        UserDetailsEntity entity = userDetailsRepository.findById(id).orElseThrow(() -> new UserDetailsNotFoundException(id));
        return UserDetailsDto.mapEntityToDto(entity);
    }

    public QueryResponseWrapper<UserDetailsDto> getUserDetails(UserDetailsSearchCriteria criteria) {
        Page<UserDetailsEntity> entityContent = userDetailsRepository.find(criteria.getFirstName(),
                criteria.getLastName(),
                criteria.getAddress(),
                criteria.getPhoneNumber(),
                criteria.getEmailAddress(),
                criteria.composePageRequest());
        Page<UserDetailsDto> dtoContent = entityContent.map(UserDetailsDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public UserDetailsDto update(UserDetailsDto userDetailsDto, Long id) {
        UserDetailsEntity entity = userDetailsRepository.findById(id).orElseThrow(() -> new UserDetailsNotFoundException(id));
        if (userDetailsDto.getFirstName() != null) {
            entity.setFirstName(userDetailsDto.getFirstName());
        }
        if (userDetailsDto.getLastName() != null) {
            entity.setLastName(userDetailsDto.getLastName());
        }
        if (userDetailsDto.getAddress() != null) {
            entity.setAddress(userDetailsDto.getAddress());
        }
        if (userDetailsDto.getPhoneNumber() != null) {
            entity.setPhoneNumber(userDetailsDto.getPhoneNumber());
        }
        if (userDetailsDto.getEmailAddress() != null) {
            entity.setEmailAddress(userDetailsDto.getEmailAddress());
        }
        UserDetailsEntity updatedUserDetails = userDetailsRepository.save(entity);
        return UserDetailsDto.mapEntityToDto(updatedUserDetails);
    }

    public void delete(Long id) {
        userDetailsRepository.deleteById(id);
    }
}
