package com.internship.bookstore.service.author;

/**
 * @author Gurgen Poghosyan
 */
public interface CreateSupported<REQUEST, RESPONSE> {

    RESPONSE create(REQUEST request);
}
