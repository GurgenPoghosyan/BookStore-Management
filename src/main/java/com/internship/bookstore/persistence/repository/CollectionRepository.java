package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.CollectionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long> {

    @Query("select c from CollectionEntity c " +
            "where (:name is null or c.name like concat('%',:name,'%'))")
    Page<CollectionEntity> find(String name, Pageable pageable);
}
