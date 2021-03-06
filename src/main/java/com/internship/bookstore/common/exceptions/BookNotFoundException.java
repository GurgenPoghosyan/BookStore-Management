package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super(String.format("Book with id: {%d} not found...", id));
    }

    public BookNotFoundException(String name) {
        super(String.format("Book with name: {%s} not found...", name));
    }
}
