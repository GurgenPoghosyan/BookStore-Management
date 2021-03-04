package com.internship.bookstore.service.book;

/**
 * @author Gurgen Poghosyan
 */
public interface CreateSupported<REQUEST, RESPONSE> {

    RESPONSE create(REQUEST request);
}
