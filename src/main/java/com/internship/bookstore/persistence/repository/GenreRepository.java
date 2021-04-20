package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    GenreEntity findByGenreName(String name);

    @Query("select g from GenreEntity g " +
            "where (:name is null or g.genreName like concat('%',:name,'%'))")
    Page<GenreEntity> find(String name, Pageable pageable);
}
