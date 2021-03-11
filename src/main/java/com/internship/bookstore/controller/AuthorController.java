package com.internship.bookstore.controller;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.service.AuthorService;
import com.internship.bookstore.service.dto.AuthorDto;
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
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorCreateRequest request){
        return authorService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id){
        return authorService.get(id);
    }

    @GetMapping
    public List<AuthorEntity> getAuthors(@RequestParam(value = "name", required = false) String name) {
        return authorService.getAuthorData(name);
    }

    @GetMapping("/{id}/books")
    public  List<BookEntity> getAuthorBooks(@PathVariable Long id){
        return authorService.getAuthorBooks(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id,
                                   @RequestBody AuthorUpdateRequest updateRequest){
        return authorService.update(updateRequest,id);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id){
        authorService.delete(id);
    }
}
