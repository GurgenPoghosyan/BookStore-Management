package com.internship.bookstore.controller;

import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.GenreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Controller
@RequestMapping("/api/v1/read-csv")
public class CSVController {

    private final CSVReaderService csvReaderService;

    @Autowired
    public CSVController(CSVReaderService csvReaderService) {
        this.csvReaderService = csvReaderService;
    }

    @PostMapping("/books")
    public List<BookDto> readBook() {
        return csvReaderService.csvBooksProcessor();
    }

    @PostMapping("/genres")
    public List<GenreDto> readGenre() {
        return csvReaderService.csvGenreProcessor();
    }

    @PostMapping("/genres-to-book")
    public void assignGenreToBook() {
        csvReaderService.csvAssignGenreToBook();
    }
}
