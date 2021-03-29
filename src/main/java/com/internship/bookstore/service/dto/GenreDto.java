package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.GenreEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gurgen Poghosyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private Long id;

    private String name;

    public static GenreEntity mapDtoToEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }
        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setGenreName(genreDto.getName());
        return genreEntity;
    }

    public static GenreDto mapEntityToDto(GenreEntity genreEntity) {
        if (genreEntity == null) {
            return null;
        }
        GenreDto dto = new GenreDto();
        dto.setId(genreEntity.getId());
        dto.setName(genreEntity.getGenreName());
        return dto;
    }
}
