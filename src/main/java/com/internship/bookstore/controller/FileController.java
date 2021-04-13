package com.internship.bookstore.controller;

import com.internship.bookstore.service.FileStorageService;
import com.internship.bookstore.service.dto.FileStorageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
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
