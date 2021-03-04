package com.internship.bookstore.service.book;

/**
 * @author Gurgen Poghosyan
 */
public interface UpdateSupported<RESPONSE, REQUEST, ID> {

    RESPONSE update(REQUEST request, ID id);
}
