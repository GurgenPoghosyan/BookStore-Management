package com.internship.bookstore.transform.response.book;

import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.genre.Genre;
import lombok.Data;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class BookResponse {

    private Long id;

    private String name;

    private String date;

    private List<Genre> genres;

    private List<Author> authors;

}
