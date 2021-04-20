package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.persistence.entity.CommunityEntity;
import com.internship.bookstore.persistence.entity.UserEntity;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String status;

    private List<CommunityDto> userCommunities = new ArrayList<>();

    private List<CollectionDto> bookCollections = new ArrayList<>();

    private UserDetailsDto details;

    private RoleDto role;

    public static UserDto mapEntityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        dto.setDetails(UserDetailsDto.mapEntityToDto(entity.getDetails()));
        dto.setRole(RoleDto.mapEntityToDto(entity.getRole()));
        List<CollectionEntity> bookCollections = entity.getBookCollections();
        if (!CollectionUtils.isEmpty(bookCollections)) {
            dto.setBookCollections(bookCollections.stream().map(CollectionDto::mapEntityToDto).collect(Collectors.toList()));
        }
        List<CommunityEntity> userCommunities = entity.getUserCommunities();
        if (!CollectionUtils.isEmpty(userCommunities)) {
            dto.setUserCommunities(userCommunities.stream().map(CommunityDto::mapEntityToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public static UserEntity mapDtoToEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setStatus(userDto.getStatus());
        return userEntity;
    }
}
