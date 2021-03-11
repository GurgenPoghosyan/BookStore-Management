package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class CollectionDto {

    private Long id;

    private String name;

    private List<BookEntity> bookEntities = new ArrayList<>();

    private GenreEntity genreEntity;
}
