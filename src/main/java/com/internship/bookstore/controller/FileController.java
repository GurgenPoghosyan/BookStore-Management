package com.internship.bookstore.controller;

import com.internship.bookstore.service.FileStorageService;
import com.internship.bookstore.service.dto.FileStorageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileStorageDto> uploadFile(@RequestParam("image") MultipartFile file,
                                                     @RequestParam("bookId") Long bookId) {
        FileStorageDto fileStorageDto = fileStorageService.storeFile(file, bookId);
        return ResponseEntity.ok(fileStorageDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id,
                                                 HttpServletRequest request) {
        String fileName = fileStorageService.getDocumentName(id);
        Resource resource = null;
            try {
                resource = fileStorageService.loadFileAsResource(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Long id) {
        fileStorageService.delete(id);
    }
}
