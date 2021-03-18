package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.service.dto.AuthorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    AuthorEntity findByName(String name);

    @Query("select new com.internship.bookstore.service.dto.AuthorDto(a.id,a.name) from AuthorEntity a")
    Page<AuthorDto> findAllWithPagination(Pageable pageable);
}
