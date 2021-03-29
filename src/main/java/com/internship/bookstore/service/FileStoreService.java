package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.FileNotFoundException;
import com.internship.bookstore.persistence.entity.FileStoreEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.FileStoreRepository;
import com.internship.bookstore.service.dto.FileStoreDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class FileStoreService {

    private final FileStoreRepository fileStoreRepository;
    private final BookRepository bookRepository;

    @Value("${file.upload-dir}")
    private String pathDirectory;


    public FileStoreDto upload(MultipartFile multipartFile) {
        FileStoreEntity fileStoreEntity = new FileStoreEntity();
        String originalFileName = multipartFile.getOriginalFilename();
            fileStoreEntity.setFileName(originalFileName.substring(0,originalFileName.lastIndexOf(".")));
            fileStoreEntity.setExtension(originalFileName.substring(originalFileName.lastIndexOf(".")));
            fileStoreEntity.setCreatedDate(LocalDateTime.now());
            fileStoreEntity.setPathDirectory(pathDirectory);
        FileStoreEntity savedFile = fileStoreRepository.save(fileStoreEntity);
        return FileStoreDto.mapEntityToDto(savedFile);
    }

    public InputStream getImage(Long id) {
        FileStoreEntity file = fileStoreRepository.findById(id).orElseThrow(()->new FileNotFoundException(id));
        return getClass().getResourceAsStream(file.getPathDirectory() + file.getFileName() + file.getExtension());
    }

    public void delete(Long id) {
        fileStoreRepository.deleteById(id);
    }
}
