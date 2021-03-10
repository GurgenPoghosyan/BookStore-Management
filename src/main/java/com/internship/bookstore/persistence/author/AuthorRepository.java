package com.internship.bookstore.persistence.author;

import com.internship.bookstore.entity.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Gurgen Poghosyan
 */

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
