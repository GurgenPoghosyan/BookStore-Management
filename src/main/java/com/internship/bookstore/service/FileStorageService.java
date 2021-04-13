package com.internship.bookstore.service;

import com.internship.bookstore.persistence.entity.FileStorageEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.FileStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileStorageRepository fileStorageRepository;

    @Autowired
    public FileStorageService(FileStorageEntity fileStorageEntity, FileStorageRepository fileStorageRepository, BookRepository bookRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.fileStorageLocation = Paths.get(fileStorageEntity.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileStorageEntity storeFile(MultipartFile multipartFile, FileStorageEntity fileStorageEntity) {
        Path targetLocation = this.fileStorageLocation.resolve(fileStorageEntity.getFileName());
        try {
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileStorageEntity.setDocumentFormat(multipartFile.getContentType());
        fileStorageEntity.setUploadDir(fileStorageLocation.toString());
        fileStorageEntity.setCreatedDate(LocalDateTime.now());
        return fileStorageRepository.save(fileStorageEntity);
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    public String getDocumentName(Long bookId) {
        return fileStorageRepository.getUploadDocumentPath(bookId);
    }

    public void delete(Long id) {
        fileStorageRepository.deleteById(id);
    }

}
