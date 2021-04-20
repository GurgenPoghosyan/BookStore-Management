package com.internship.bookstore.common.threads;

import com.internship.bookstore.persistence.entity.FileStorageEntity;
import com.internship.bookstore.service.CSVReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class Multithreading {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private CSVReaderService csvReaderService;

    public void parallelExec(MultipartFile file, FileSaveThread thread) throws Exception {
        List<CompletableFuture<List<FileStorageEntity>>> futures = new ArrayList<>();
        run(file, thread);

        CompletableFuture[] completableFutures = futures.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(completableFutures);

        for (CompletableFuture completableFuture : completableFutures) {
            try {
                System.out.printf("Number retrieved from thread %s%n", completableFuture.get());
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
    }

    private void run(MultipartFile file, FileSaveThread thread) {
        CompletableFuture.runAsync(thread, executorService);
    }

//    public void parallelExec(File file,UserSaveThread thread) throws Exception {
//        List<CompletableFuture<List<FileStorageEntity>>> futures = new ArrayList<>();
//        // From logs you can notice that tasks are exec in async mode.
//        // Same way you can parse your csv parts parallel or upload images
//        run(file,thread);
//
//        CompletableFuture[] completableFutures = futures.toArray(new CompletableFuture[0]);
//        CompletableFuture.allOf(completableFutures);
//
//        // Here I get all completed tasks, tho its not necessary to return tasks
//        // You can do whatever is needed inside getFutureTasks() and do not return anything, its up to you
//        for (CompletableFuture completableFuture : completableFutures) {
//            try {
//                System.out.printf("Number retrieved from thread %s%n", completableFuture.get());
//            } catch (InterruptedException | ExecutionException ignored) {
//            }
//        }
//    }
//
//    private Future<Void> run(File file, UserSaveThread thread){
//        return CompletableFuture.runAsync(thread,executorService);
//    }
//    private CompletableFuture<List<FileStorageEntity>> getFutureTask(File file) {
//        return CompletableFuture.supplyAsync(() -> csvReaderService.saveFileFromUrl(file), executorService);
//    }
}
