package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {
    CollectionEntity findByName(String name);
}
