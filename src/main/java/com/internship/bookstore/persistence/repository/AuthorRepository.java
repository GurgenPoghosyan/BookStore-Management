package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}
