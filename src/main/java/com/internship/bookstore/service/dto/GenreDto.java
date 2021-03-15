package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.GenreEntity;
import lombok.Data;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class GenreDto {

    private Long id;

    private String genreName;

    public static GenreDto mapEntityToDto(GenreEntity genreEntity){
        if(genreEntity==null){
            return null;
        }
        GenreDto dto = new GenreDto();
        dto.setId(genreEntity.getId());
        dto.setGenreName(genreEntity.getGenreName());
        return dto;
    }
}
