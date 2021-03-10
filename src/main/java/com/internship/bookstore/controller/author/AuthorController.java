package com.internship.bookstore.controller.author;

import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.book.Book;
import com.internship.bookstore.service.author.AuthorService;
import com.internship.bookstore.transform.request.author.AuthorCreateRequest;
import com.internship.bookstore.transform.request.author.AuthorUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping()
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorCreateRequest request){
        return authorService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable Long id){
        return authorService.get(id);
    }

    @GetMapping
    public List<Author> getAuthors(@RequestParam(value = "name", required = false) String name) {
        return authorService.getAuthorData(name);
    }

    @GetMapping("/{id}/books")
    public  List<Book> getAuthorBooks(@PathVariable Long id){
        return authorService.getAuthorBooks(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateBook(@PathVariable Long id,
                                   @RequestBody AuthorUpdateRequest updateRequest){
        return authorService.update(updateRequest,id);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id){
        authorService.delete(id);
    }
}
