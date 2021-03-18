package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.service.dto.CollectionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {

    Page<CollectionDto> findAllWithPagination(Pageable pageable);
}
