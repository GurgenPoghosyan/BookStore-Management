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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final CSVReaderService csvReaderService;

    @PostMapping
    @PreAuthorize("hasRole('EDITOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR','USER')")
    public QueryResponseWrapper<BookDto> getBooks(BookSearchCriteria criteria) {
        return bookService.getBooks(criteria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EDITOR')")
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
    @PreAuthorize("hasAnyRole('EDITOR','USER')")
    public List<AuthorDto> getBooksAuthors(@PathVariable Long id) {

        return bookService.getBooksAuthors(id);
    }

    @GetMapping("/{id}/genres")
    @PreAuthorize("hasAnyRole('EDITOR','USER')")
    public List<GenreDto> getBooksGenres(@PathVariable Long id) {
        return bookService.getBooksGenres(id);
    }

    @PostMapping("/{id}/authors")
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<BookDto> addAuthorToBook(@PathVariable Long id, @RequestBody AddAuthorRequestBody requestBody) {
        BookDto dto = bookService.addAuthorToBook(id, requestBody.getAuthorId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/genres")
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<BookDto> addGenreToBook(@PathVariable Long id, @RequestBody AddGenreRequestBody requestBody) {
        BookDto dto = bookService.addGenreToBook(id, requestBody.getGenreId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public void uploadBooks(@RequestParam("books") MultipartFile multipartFile) {
        csvReaderService.csvBooksProcessor(multipartFile);
    }

//    @PostMapping("/upload")
//    public void uploadBooks(@RequestParam("books") MultipartFile multipartFile) {
//        bookService.saveBooks(multipartFile);
//    }

    @PostMapping("/genres/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public void uploadGenres(@RequestParam("genre-to-book") MultipartFile multipartFile) {
        csvReaderService.csvAssignGenreToBook(multipartFile);
    }
}
