package com.internship.bookstore.service;

/**
 * @author Gurgen Poghosyan
 */

public interface CRUDService<T> {

    T create(T requestT);

    T get(Long id);

    T update(T requestT, Long id);

    void delete(Long id);
}
