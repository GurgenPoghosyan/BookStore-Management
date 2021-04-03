package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.FileStorageEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.FileStorageRepository;
import com.internship.bookstore.service.dto.FileStorageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileStorageRepository fileStorageRepository;
    private final BookRepository bookRepository;

    @Autowired
    public FileStorageService(FileStorageEntity fileStorageEntity, FileStorageRepository fileStorageRepository, BookRepository bookRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.fileStorageLocation = Paths.get(fileStorageEntity.getUploadDir()).toAbsolutePath().normalize();
        this.bookRepository = bookRepository;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileStorageDto storeFile(MultipartFile multipartFile, Long bookId) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        fileName = bookId + extension;
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        try {
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileStorageEntity newDoc = new FileStorageEntity();
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        newDoc.setFileName(fileName);
        newDoc.setDocumentFormat(multipartFile.getContentType());
        newDoc.setBook(bookEntity);
        newDoc.setExtension(extension);
        newDoc.setUploadDir(fileStorageLocation.toString());
        newDoc.setCreatedDate(LocalDateTime.now());
        FileStorageEntity savedFile = fileStorageRepository.save(newDoc);
        bookEntity.setBookCoverImage(savedFile);
        bookRepository.save(bookEntity);
        return FileStorageDto.mapEntityToDto(savedFile);
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
