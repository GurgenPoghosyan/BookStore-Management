package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Gurgen Poghosyan
 */
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    GenreEntity findByGenreName(String name);
}
