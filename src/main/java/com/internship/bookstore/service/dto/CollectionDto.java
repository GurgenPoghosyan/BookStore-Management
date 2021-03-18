package com.internship.bookstore.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class CollectionDto {

    private Long id;

    private String name;

    private List<Long> books;
}
