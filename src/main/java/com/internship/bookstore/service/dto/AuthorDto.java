package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gurgen Poghosyan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private Long id;

    private String name;

    public static AuthorDto mapEntityToDto(AuthorEntity entity) {
        if (entity == null) {
            return null;
        }
        AuthorDto dto = new AuthorDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static AuthorEntity mapDtoToEntity(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(authorDto.getName());
        return authorEntity;
    }

}
