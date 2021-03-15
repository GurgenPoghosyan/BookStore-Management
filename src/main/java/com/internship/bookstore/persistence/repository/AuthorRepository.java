package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.service.model.AuthorWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    @Query("SELECT new com.internship.bookstore.service.model.AuthorWrapper(u) FROM AuthorEntity u")
    List<AuthorWrapper> findAllAuthors();
}
