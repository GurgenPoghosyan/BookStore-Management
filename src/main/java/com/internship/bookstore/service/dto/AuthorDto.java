package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import lombok.Data;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class AuthorDto {

    private Long id;

    private String name;

}
