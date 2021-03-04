package com.internship.bookstore.service.book;

/**
 * @author Gurgen Poghosyan
 */

public interface GetSupported<ID,RESPONSE> {

    RESPONSE get(ID id);
}
