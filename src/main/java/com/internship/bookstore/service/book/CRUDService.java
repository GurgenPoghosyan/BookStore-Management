package com.internship.bookstore.service.book;

/**
 * @author Gurgen Poghosyan
 */
public interface CRUDService<CREQUEST,UREQUEST, RESPONSE, ID> {

    RESPONSE create(CREQUEST request);

    RESPONSE get(ID id);

    RESPONSE update(UREQUEST request, ID id);

    void delete(ID id);
}
