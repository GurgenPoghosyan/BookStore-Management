package com.internship.bookstore.controller;

import com.internship.bookstore.service.AuthorService;
import com.internship.bookstore.service.criteria.AuthorSearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
@PreAuthorize("hasAuthority('EDITOR')")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping()
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto dto = authorService.create(authorDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id) {
        AuthorDto dto = authorService.getAuthor(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<AuthorDto> getAuthors(AuthorSearchCriteria criteria) {
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
