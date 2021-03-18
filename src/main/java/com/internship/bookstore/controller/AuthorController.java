package com.internship.bookstore.controller;

import com.internship.bookstore.service.AuthorService;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.model.AuthorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping()
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto dto = authorService.create(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id) {
        AuthorDto dto = authorService.get(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<AuthorWrapper> getAuthors(@RequestParam(value = "name", required = false) String name) {
        return authorService.getAuthorData(name);
    }

//    @GetMapping("/{id}/books")
//    public List<BookDto> getAuthorBooks(@PathVariable Long id) {
//        return authorService.getAuthorBooks(id);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id,
                                                  @RequestBody AuthorDto authorDto) {
        AuthorDto dto = authorService.update(authorDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
    }
}