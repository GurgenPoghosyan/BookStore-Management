package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.FileStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Gurgen Poghosyan
 */
public interface FileStoreRepository extends JpaRepository<FileStoreEntity,Long> {
}
