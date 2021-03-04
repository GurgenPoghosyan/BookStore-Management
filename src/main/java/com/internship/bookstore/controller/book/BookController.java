package com.internship.bookstore.controller.book;

import com.internship.bookstore.entity.book.Book;
import com.internship.bookstore.service.book.BookService;
import com.internship.bookstore.transform.request.book.BookCreateRequest;
import com.internship.bookstore.transform.request.book.BookUpdateRequest;
import com.internship.bookstore.transform.response.book.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public BookResponse createBook(@RequestBody BookCreateRequest request){
        return bookService.create(request);
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Long id){
        return bookService.get(id);
    }

    @GetMapping()
    public List<Book> getBooks(@RequestParam(value = "name", required = false) String name) {
        return bookService.getBookData(name);
    }
    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id,
                                   @RequestBody BookUpdateRequest updateRequest){
        return bookService.update(updateRequest,id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.delete(id);
    }


}
