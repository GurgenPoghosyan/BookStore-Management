package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class UserDetailsNotFoundException extends RuntimeException {

    public UserDetailsNotFoundException(Long id) {
        super(String.format("Details with id: {%d} not found...", id));
    }

    public UserDetailsNotFoundException(String name) {
        super(String.format("Details with name: {%s} not found...", name));
    }
}
