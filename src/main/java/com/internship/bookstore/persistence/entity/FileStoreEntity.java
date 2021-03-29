package com.internship.bookstore.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "upload_dir")
    private String pathDirectory;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;
}
