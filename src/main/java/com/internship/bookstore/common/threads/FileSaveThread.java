package com.internship.bookstore.common.threads;

import com.internship.bookstore.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gurgen Poghosyan
 */
@RequiredArgsConstructor
public class FileSaveThread implements Runnable {

    private final FileStorageService fileStorageService;
    private final MultipartFile csvFile;

    @Override
    public void run() {
        saveAll(csvFile);
    }

    private void saveAll(MultipartFile csvFile) {
        fileStorageService.saveFileFromUrl(csvFile);
    }
}
