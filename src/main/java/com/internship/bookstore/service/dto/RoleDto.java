package com.internship.bookstore.service.dto;

import com.internship.bookstore.common.enums.Role;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gurgen Poghosyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Long id;

    private String name;

    public static RoleDto mapEntityToDto(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        RoleDto dto = new RoleDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static RoleEntity mapDtoToEntity(RoleDto roleDto) {
        if (roleDto == null) {
            return null;
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleDto.getName());
        return roleEntity;
    }

}
