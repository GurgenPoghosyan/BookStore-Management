package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.service.dto.CollectionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {

    @Query("select new com.internship.bookstore.service.dto.CollectionDto(c.id,c.name) from CollectionEntity c")
    Page<CollectionDto> findAllWithPagination(Pageable pageable);
}
