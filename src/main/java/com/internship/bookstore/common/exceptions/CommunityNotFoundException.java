package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class CommunityNotFoundException extends RuntimeException {

    public CommunityNotFoundException(Long id) {
        super(String.format("Community with id: {%d} not found...", id));
    }

    public CommunityNotFoundException(String name) {
        super(String.format("Community with name: {%s} not found...", name));
    }
}
