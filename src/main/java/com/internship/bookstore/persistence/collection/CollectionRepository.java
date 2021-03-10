package com.internship.bookstore.persistence.collection;

import com.internship.bookstore.entity.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */
public interface CollectionRepository extends JpaRepository<Collection,Long> {
    Collection findByName(String name);
}
