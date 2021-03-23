package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.FileNotFoundException;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.FileStoreEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.FileStoreRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class FileStoreService {

    private final FileStoreRepository fileStoreRepository;
    private final BookRepository bookRepository;

    @Value("${file.upload-dir}")
    private String pathDirectory;

    @Autowired
    public FileStoreService(FileStoreRepository fileStoreRepository, BookRepository bookRepository) {
        this.fileStoreRepository = fileStoreRepository;
        this.bookRepository = bookRepository;
    }

    public byte[] getImage(Long id) throws IOException {
        FileStoreEntity fileStoreEntity = fileStoreRepository.findById(id).orElseThrow(()->new FileNotFoundException(id));
        InputStream in = getClass()
                .getResourceAsStream(fileStoreEntity.getFilePath());
        return IOUtils.toByteArray(in);
    }

}
