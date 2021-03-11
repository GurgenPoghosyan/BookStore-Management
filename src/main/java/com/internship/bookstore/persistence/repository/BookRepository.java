package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
