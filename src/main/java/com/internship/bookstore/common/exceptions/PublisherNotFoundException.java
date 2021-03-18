package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException(Long id) {
        super(String.format("Publisher with id: {%d} not found...", id));
    }

    public PublisherNotFoundException(String name) {
        super(String.format("Publisher with name: {%s} not found...", name));
    }
}
