package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Data
@NoArgsConstructor
public class BookDto {

    private Long id;

    private String name;

    private String date;

    private Double rating;

    private String language;

    private Integer pages;

    private Long publisherId;

    private String isbn;

    private List<Long> genres;

    private List<Long> authors;

    public static BookDto mapEntityToDto(BookEntity entity) {
        if (entity == null) {
            return null;
        }
        BookDto dto = new BookDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        dto.setPages(entity.getPages());
        dto.setRating(entity.getRating());
        dto.setIsbn(entity.getIsbn());
        dto.setPublisherId(entity.getPublisher().getId());
        List<AuthorEntity> listOfAuthors = entity.getAuthors();
        if (!CollectionUtils.isEmpty(listOfAuthors)) {
            dto.setAuthors(listOfAuthors.stream().map(AuthorEntity::getId).collect(Collectors.toList()));
        }
        List<GenreEntity> listOfGenres = entity.getGenres();
        if (!CollectionUtils.isEmpty(listOfGenres)) {
            dto.setGenres(listOfGenres.stream().map(GenreEntity::getId).collect(Collectors.toList()));
        }

        return dto;
    }

    public static BookEntity mapDtoToEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName(bookDto.getName());
        bookEntity.setPages(bookDto.getPages());
        bookEntity.setDate(bookDto.getDate());
        bookEntity.setRating(bookDto.getRating());
        bookEntity.setIsbn(bookDto.getIsbn());
        return bookEntity;
    }
}
