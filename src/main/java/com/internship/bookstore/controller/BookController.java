package com.internship.bookstore.controller;

import com.internship.bookstore.service.BookService;
import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.criteria.BookSearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import com.internship.bookstore.transform.requestbody.book.AddAuthorRequestBody;
import com.internship.bookstore.transform.requestbody.book.AddGenreRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final CSVReaderService csvReaderService;

    @Autowired
    public BookController(BookService bookService, CSVReaderService csvReaderService) {
        this.bookService = bookService;
        this.csvReaderService = csvReaderService;
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto dto = bookService.create(bookDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        BookDto dto = bookService.getBook(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<BookDto> getBooks(@RequestBody BookSearchCriteria criteria) {
        return bookService.getBooks(criteria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                              @RequestBody BookDto bookDto) {
        BookDto dto = bookService.update(bookDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/{id}/authors")
    public List<AuthorDto> getBooksAuthors(@PathVariable Long id) {
        return bookService.getBooksAuthors(id);
    }

    @GetMapping("/{id}/genres")
    public List<GenreDto> getBooksGenres(@PathVariable Long id) {
        return bookService.getBooksGenres(id);
    }

    @PostMapping("/{id}/authors")
    public ResponseEntity<BookDto> addAuthorToBook(@PathVariable Long id, @RequestBody AddAuthorRequestBody requestBody) {
        BookDto dto = bookService.addAuthorToBook(id, requestBody.getAuthorId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/genres")
    public ResponseEntity<BookDto> addGenreToBook(@PathVariable Long id, @RequestBody AddGenreRequestBody requestBody) {
        BookDto dto = bookService.addGenreToBook(id, requestBody.getGenreId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/read-csv")
    public void readBook(@RequestParam("books") MultipartFile multipartFile) {
        csvReaderService.csvBooksProcessor(multipartFile);
    }

    @PostMapping("/read-genre-to-book-csv")
    public void assignGenreToBook(@RequestParam("genre-to-book") MultipartFile multipartFile) {
        csvReaderService.csvAssignGenreToBook(multipartFile);
    }
}
