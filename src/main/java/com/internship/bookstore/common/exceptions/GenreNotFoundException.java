package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(Long id) {
        super(String.format("GenreEntity with id: {%d} not found...", id));
    }

    public GenreNotFoundException(String name) {
        super(String.format("GenreEntity with name: {%s} not found...", name));
    }
}