package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(Long id) {
        super(String.format("Role with id: {%d} not found...", id));
    }

    public RoleNotFoundException(String name) {
        super(String.format("Role with name: {%s} not found...", name));
    }
}