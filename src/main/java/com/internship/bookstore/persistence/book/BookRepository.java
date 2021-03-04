package com.internship.bookstore.persistence.book;

import org.springframework.data.jpa.repository.JpaRepository;

import com.internship.bookstore.entity.book.Book;

/**
 * @author Gurgen Poghosyan
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
