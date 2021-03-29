package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.service.dto.AuthorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Gurgen Poghosyan
 */

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    AuthorEntity findByName(String name);

    @Query("select a from AuthorEntity a " +
            "where (:name is null or a.name like concat('%',:name,'%'))")
    Page<AuthorEntity> find(String name, Pageable pageable);
}
