package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class BookDto {

    private Long id;

    private String name;

    private String date;

    private List<GenreDto> genres;

    private List<AuthorDto> authors;

    public static BookDto mapEntityToDto(BookEntity entity){
        if (entity==null) {
            return null;
        }
        BookDto dto= new BookDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        List<AuthorEntity> listOfAuthors = entity.getAuthors();
        if(!CollectionUtils.isEmpty(listOfAuthors)){
            dto.setAuthors(listOfAuthors.stream().map(AuthorDto::mapEntityToDto).collect(Collectors.toList()));
        }
        List<GenreEntity> listOfGenres = entity.getGenres();
        if(!CollectionUtils.isEmpty(listOfGenres)){
            dto.setGenres(listOfGenres.stream().map(GenreDto::mapEntityToDto).collect(Collectors.toList()));
        }
        return dto;
    }
}
