package com.internship.bookstore.service;

/**
 * @author Gurgen Poghosyan
 */
public interface CRUDService<BODY, ID> {

    BODY create(BODY requestBody);

    BODY get(ID id);

    BODY update(BODY requestBody, ID id);

    void delete(ID id);
}
