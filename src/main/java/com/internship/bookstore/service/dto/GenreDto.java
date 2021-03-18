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
}
