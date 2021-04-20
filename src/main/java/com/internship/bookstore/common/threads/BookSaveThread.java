package com.internship.bookstore.common.threads;

import com.internship.bookstore.common.util.CsvParser;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RequiredArgsConstructor
public class BookSaveThread implements Runnable {
    private final BookService bookService;
    private final CsvParser csvParser;
    private final File csvFile;

    @Override
    public void run() {
        saveAll(csvFile);
    }

    private void saveAll(File csvFile) {
        List<BookEntity> entities = csvParser.parseBookCsv(csvFile);
        bookService.saveAll(entities);
    }
}
