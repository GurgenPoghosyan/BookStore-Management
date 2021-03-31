package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.FileStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * @author Gurgen Poghosyan
 */
public interface FileStorageRepository extends JpaRepository<FileStorageEntity, Long> {

    @Query("Select f.fileName from FileStorageEntity f where f.id = :id")
    String getUploadDocumentPath(Long id);
}
