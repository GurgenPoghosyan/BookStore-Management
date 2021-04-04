package com.internship.bookstore.common.exceptions;


/**
 * @author Gurgen Poghosyan
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(String.format("User with id: {%d} not found...", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("User with username: {%s} not found...", username));
    }
}
