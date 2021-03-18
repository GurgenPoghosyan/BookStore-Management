package com.internship.bookstore.controller;

import com.internship.bookstore.service.BookService;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.model.BookWrapper;
import com.internship.bookstore.transform.request.book.AddAuthorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto dto = bookService.create(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        BookDto dto = bookService.get(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<BookWrapper> getBooks(@RequestParam(value = "name", required = false) String name) {
        return bookService.getBookData(name);
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
    public List<AuthorDto> getBookAuthors(@PathVariable Long id) {
        return bookService.getBookAuthors(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<BookDto> addAuthorToBook(@PathVariable Long id,@RequestBody AddAuthorRequest request) {
        BookDto dto = bookService.addAuthorToBook(id, request.getAuthorId());
        return ResponseEntity.ok(dto);
    }
}
