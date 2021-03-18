package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class CollectionNotFoundException extends RuntimeException {

    public CollectionNotFoundException(Long id) {
        super(String.format("Collection with id: {%d} not found...", id));
    }

    public CollectionNotFoundException(String name) {
        super(String.format("Collection with name: {%s} not found...", name));
    }
}
