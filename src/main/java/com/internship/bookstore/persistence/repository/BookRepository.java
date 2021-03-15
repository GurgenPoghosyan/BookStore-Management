package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.service.model.BookWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("SELECT new com.internship.bookstore.service.model.BookWrapper(u) FROM BookEntity u")
    List<BookWrapper> findAllBooks();
}
