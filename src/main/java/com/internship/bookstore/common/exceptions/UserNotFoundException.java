package com.internship.bookstore.common.exceptions;


/**
 * @author Gurgen Poghosyan
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(String.format("User with id: {%d} not found...", id));
    }

    public UserNotFoundException(String name) {
        super(String.format("User with name: {%s} not found...", name));
    }
}
