package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    PublisherEntity findByName(String name);
}
