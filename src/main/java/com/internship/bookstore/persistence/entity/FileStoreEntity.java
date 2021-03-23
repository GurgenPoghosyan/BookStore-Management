package com.internship.bookstore.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
public class FileStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "book_cover")
    private String bookCover;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "upload_dir")
    private String pathDirectory;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
