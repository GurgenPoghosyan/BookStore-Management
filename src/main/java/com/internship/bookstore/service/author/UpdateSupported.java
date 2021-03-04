package com.internship.bookstore.service.author;

/**
 * @author Gurgen Poghosyan
 */
public interface UpdateSupported<RESPONSE, REQUEST, ID> {

    RESPONSE update(REQUEST request, ID id);
}
