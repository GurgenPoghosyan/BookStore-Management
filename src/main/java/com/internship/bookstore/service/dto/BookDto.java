package com.internship.bookstore.service.dto;

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

    private Double rating;

    private String language;

    private Integer pages;

    private Long publisherId;

    private List<Long> genres;

    private List<Long> authors;

}
