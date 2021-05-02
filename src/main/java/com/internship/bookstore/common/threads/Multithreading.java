package com.internship.bookstore.common.threads;

import com.internship.bookstore.persistence.entity.FileStorageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class Multithreading {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void parallelExec(MultipartFile file, FileSaveThread thread) throws Exception {
        List<CompletableFuture<List<FileStorageEntity>>> futures = new ArrayList<>();
        run(file, thread);

    }

    private void run(MultipartFile file, FileSaveThread thread) {
        CompletableFuture.runAsync(thread, executorService);
    }
}
