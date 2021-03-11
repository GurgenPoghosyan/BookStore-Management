package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class BookDto {

    private Long id;

    private String name;

    private String date;

    private List<GenreEntity> genreEntities;

    private List<AuthorEntity> authorEntities;

}
