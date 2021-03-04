package com.internship.bookstore.transform.response.author;

import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.book.Book;
import lombok.Data;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class AuthorResponse {

    private Long id;

    private String name;

    private String surname;

    private List<Book> books;

}
