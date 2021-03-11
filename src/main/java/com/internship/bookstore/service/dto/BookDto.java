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

    private List<String> genres;

    private List<String> authors;

    public static BookDto mapEntityToDto(BookEntity entity){
        if (entity==null) {
            return null;
        }
        BookDto dto= new BookDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        List<GenreEntity> listOfGenres = entity.getGenres();
        if(!CollectionUtils.isEmpty(listOfGenres)){
            dto.setGenres(listOfGenres.stream().map(GenreEntity::getGenreName).collect(Collectors.toList()));
        }
        List<AuthorEntity> listOfAuthors = entity.getAuthors();
        if(!CollectionUtils.isEmpty(listOfAuthors)){
            dto.setAuthors(listOfAuthors.stream().map(e->listOfAuthors.toString()).collect(Collectors.toList()));
        }
        return dto;
    }
}
