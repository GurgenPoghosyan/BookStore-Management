package com.internship.bookstore.controller;

import com.internship.bookstore.service.FileStoreService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/{id}")
    public @ResponseBody byte[] getImage(@PathVariable Long id) throws IOException {
        return fileStoreService.getImage(id);
    }
}
