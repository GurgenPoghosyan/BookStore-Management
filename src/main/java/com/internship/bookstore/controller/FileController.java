package com.internship.bookstore.controller;

import com.internship.bookstore.service.FileStoreService;
import com.internship.bookstore.service.dto.FileStoreDto;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileStoreService fileStoreService;

    @Autowired
    public FileController(FileStoreService fileStoreService) {
        this.fileStoreService = fileStoreService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileStoreDto> upload(@RequestParam(value = "image") MultipartFile multipartFile) {
        FileStoreDto fileStoreDto = fileStoreService.upload(multipartFile);
        return ResponseEntity.ok(fileStoreDto);
    }

    @GetMapping(value = "/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public
    byte[] getImageWithMediaType(@PathVariable Long id) throws IOException {
        InputStream in = fileStoreService.getImage(id);
        return IOUtils.toByteArray(in);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Long id) {
        fileStoreService.delete(id);
    }
}
