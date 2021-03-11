package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(Long id) {
        super(String.format("AuthorEntity with id: {%d} not found...", id));
    }

    public AuthorNotFoundException(String name) {
        super(String.format("AuthorEntity with name: {%s} not found...", name));
    }
}