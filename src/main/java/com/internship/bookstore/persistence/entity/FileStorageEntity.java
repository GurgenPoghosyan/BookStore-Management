package com.internship.bookstore.persistence.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Gurgen Poghosyan
 */
@Entity
@Table(name = "file_store")
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "file")
public class FileStorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;

    @Value("{file.upload-dir}")
    @Column(name = "upload_dir")
    private String uploadDir;
}
