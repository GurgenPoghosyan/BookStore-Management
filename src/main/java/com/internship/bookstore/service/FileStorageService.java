package com.internship.bookstore.service;

import com.internship.bookstore.persistence.entity.FileStorageEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.FileStorageRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public String getDocumentName(Long id) {
        return fileStorageRepository.getUploadDocumentPath(id);
    }

    public List<FileStorageEntity> saveFileFromUrl(MultipartFile multipartFile) {
        List<FileStorageEntity> files = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withQuote(null).withDelimiter(';'))) {

            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                String extension = ".jpg";
                FileStorageEntity fileStoreEntity = new FileStorageEntity();
                String fileUrl = csvRecord.get("Image-URL-S");
                String fileName = System.currentTimeMillis() + extension;
                Path path = this.fileStorageLocation.resolve(fileName);
                File file = new File(String.valueOf(path));
                FileUtils.copyURLToFile(new URL(fileUrl), file);
                fileStoreEntity.setDocumentFormat(Files.probeContentType(path));
                fileStoreEntity.setExtension(extension);
                fileStoreEntity.setFileName(fileName);
                fileStoreEntity.setUploadDir(fileStorageLocation.toString());
                fileStoreEntity.setCreatedDate(LocalDateTime.now());
                FileStorageEntity savedFile = fileStorageRepository.save(fileStoreEntity);
                files.add(savedFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

}
