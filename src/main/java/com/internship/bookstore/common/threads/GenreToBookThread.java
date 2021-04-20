package com.internship.bookstore.common.threads;

import com.internship.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * @author Gurgen Poghosyan
 */
@RequiredArgsConstructor
public class GenreToBookThread implements Runnable {
    private final BookService bookService;
    private final File csvFile;

    @Override
    public void run() {
        saveAll(csvFile);
    }

    private void saveAll(File csvFile) {
        bookService.csvAssignGenreToBook(csvFile);
    }
}
