package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    GenreEntity findByGenreName(String name);
}
