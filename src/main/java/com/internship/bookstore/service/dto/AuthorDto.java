package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.BookEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class AuthorDto {

    private Long id;

    private String name;

    private String surname;

    private List<BookEntity> bookEntities;

}
