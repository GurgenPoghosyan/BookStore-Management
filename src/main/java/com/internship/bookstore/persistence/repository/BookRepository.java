package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.service.model.BookWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("SELECT new com.internship.bookstore.service.model.BookWrapper(b) FROM BookEntity b")
    List<BookWrapper> findAllBooks();

    @Query("select new com.internship.bookstore.service.model.BookWrapper(b.id,b.name,b.rating)  from BookEntity b")
    Page<BookWrapper> findAllWithPagination(Pageable pageable);

    BookEntity findByIsbn(String isbn);
}
