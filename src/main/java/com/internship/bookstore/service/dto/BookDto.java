package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.entity.PublisherEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.persistence.repository.GenreRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Double rating;

    private String language;

    private Integer pages;

    private Long publisherId;

    private List<Long> genres;

    private List<Long> authors;


}
