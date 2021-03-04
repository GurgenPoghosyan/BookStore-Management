package com.internship.bookstore.service.author;

/**
 * @author Gurgen Poghosyan
 */

public interface GetSupported<ID,RESPONSE> {

    RESPONSE get(ID id);
}
