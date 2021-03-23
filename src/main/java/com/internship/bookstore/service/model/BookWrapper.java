package com.internship.bookstore.service.model;

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
public class BookWrapper {
    private Long id;
    private String name;
    private String language;
    private Double rating;
    private List<String> genres;

    public BookWrapper(Long id, String name, Double rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public BookWrapper(BookEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.rating = entity.getRating();
        List<GenreEntity> genres = entity.getGenres();
        if (!CollectionUtils.isEmpty(genres)) {
            this.genres = genres.stream().map(GenreEntity::getGenreName).collect(Collectors.toList());
        }
    }
}
