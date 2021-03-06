package com.internship.bookstore.common.exceptions;

import com.internship.bookstore.entity.author.Author;

/**
 * @author Gurgen Poghosyan
 */
public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(Long id) {
        super(String.format("Author with id: {%d} not found...", id));
    }

    public AuthorNotFoundException(String name) {
        super(String.format("Author with name: {%s} not found...", name));
    }
}