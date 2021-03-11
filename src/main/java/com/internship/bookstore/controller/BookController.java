package com.internship.bookstore.controller;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.service.BookService;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.transform.request.book.AddAuthorRequest;
import com.internship.bookstore.transform.request.book.BookCreateRequest;
import com.internship.bookstore.transform.request.book.BookUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookCreateRequest request) {
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        return bookService.get(id);
    }

    @GetMapping
    public List<BookEntity> getBooks(@RequestParam(value = "name", required = false) String name) {
        return bookService.getBookData(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                           @RequestBody BookUpdateRequest updateRequest) {
        return bookService.update(updateRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/{id}/authors")
    public  List<AuthorEntity> getBookAuthors(@PathVariable Long id){
        return bookService.getBookAuthors(id);
    }

    @PutMapping()
    public ResponseEntity<BookDto> addGenreToBook(@RequestBody AddAuthorRequest request){
        return  bookService.addAuthorToBook(request.getBookId(), request.getAuthorId());
    }
}
