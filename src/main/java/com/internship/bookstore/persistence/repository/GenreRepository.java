package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.service.dto.GenreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    GenreEntity findByGenreName(String name);

    @Query("select new com.internship.bookstore.service.dto.GenreDto(g.id,g.genreName) from GenreEntity g")
    Page<GenreDto> findAllWithPagination(Pageable pageable);
}
