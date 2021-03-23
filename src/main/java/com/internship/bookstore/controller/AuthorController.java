package com.internship.bookstore.controller;

import com.internship.bookstore.service.AuthorService;
import com.internship.bookstore.service.criteria.SearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping()
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto dto = authorService.create(authorDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id) {
        AuthorDto dto = authorService.get(id);
        return ResponseEntity.ok(dto);
    }

    //todo use single method for getting authors with different search params, instead of separate routes
    @GetMapping
    public List<AuthorDto> getAuthors(@RequestParam(value = "name", required = false) String name) {
        return authorService.getAuthors(name);
    }

    @GetMapping("/with-pagination")
    public QueryResponseWrapper<AuthorDto> getAuthors(SearchCriteria criteria) {
        return authorService.getAuthors(criteria);
    }

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
