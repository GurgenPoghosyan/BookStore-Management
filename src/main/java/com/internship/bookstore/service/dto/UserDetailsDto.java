package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.UserDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gurgen Poghosyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String emailAddress;
    //todo id or user????
    private Long userId;

    public static UserDetailsDto mapEntityToDto(UserDetailsEntity entity) {
        if (entity == null) {
            return null;
        }
        UserDetailsDto dto = new UserDetailsDto();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setUserId(entity.getUser().getId());
        dto.setEmailAddress(entity.getEmailAddress());

        return dto;
    }

    public static UserDetailsEntity mapDtoToEntity(UserDetailsDto dto) {
        if (dto == null) {
            return null;
        }
        UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
        userDetailsEntity.setFirstName(dto.getFirstName());
        userDetailsEntity.setLastName(dto.getLastName());
        userDetailsEntity.setAddress(dto.getAddress());
        userDetailsEntity.setPhoneNumber(dto.getPhoneNumber());
        userDetailsEntity.setEmailAddress(dto.getEmailAddress());
        return userDetailsEntity;
    }
}
