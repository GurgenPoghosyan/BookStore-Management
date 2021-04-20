package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.PublisherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    PublisherEntity findByName(String name);

    @Query("select p from PublisherEntity p " +
            "where (:name is null or p.name like concat('%',:name,'%'))")
    Page<PublisherEntity> find(String name, Pageable pageable);
}
