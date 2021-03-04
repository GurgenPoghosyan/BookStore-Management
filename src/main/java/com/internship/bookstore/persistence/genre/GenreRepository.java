package com.internship.bookstore.persistence.genre;

import com.internship.bookstore.entity.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Gurgen Poghosyan
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByGenreName(String name);
}
