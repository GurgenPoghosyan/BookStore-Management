package com.internship.bookstore.common.threads;

import com.internship.bookstore.common.util.CsvParser;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.service.GenreService;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RequiredArgsConstructor
public class GenreSaveThread implements Runnable {
    private final GenreService genreService;
    private final CsvParser csvParser;
    private final File csvFile;

    @Override
    public void run() {
        saveAll(csvFile);
    }

    private void saveAll(File csvFile) {
        List<GenreEntity> entities = csvParser.parseGenreCsv(csvFile);
        genreService.saveAll(entities);
    }
}
