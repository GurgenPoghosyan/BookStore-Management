package com.internship.bookstore.common.exceptions;

import com.internship.bookstore.entity.genre.Genre;

/**
 * @author Gurgen Poghosyan
 */
public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(Long id) {
        super(String.format("Genre with id: {%d} not found...", id));
    }

    public GenreNotFoundException(String name) {
        super(String.format("Genre with name: {%s} not found...", name));
    }
}