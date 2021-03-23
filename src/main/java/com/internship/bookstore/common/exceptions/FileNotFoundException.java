package com.internship.bookstore.common.exceptions;

/**
 * @author Gurgen Poghosyan
 */
public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(Long id) {
        super(String.format("File with id: {%d} not found...", id));
    }

    public FileNotFoundException(String name) {
        super(String.format("File with name: {%s} not found...", name));
    }
}