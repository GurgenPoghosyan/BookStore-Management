package com.internship.bookstore.controller.author;

import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.service.author.AuthorService;
import com.internship.bookstore.transform.request.author.AuthorCreateRequest;
import com.internship.bookstore.transform.request.author.AuthorUpdateRequest;
import com.internship.bookstore.transform.response.author.AuthorResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public AuthorResponse createAuthor(@RequestBody AuthorCreateRequest request){
        return authorService.create(request);
    }

    @GetMapping("/{id}")
    public AuthorResponse getAuthor(@PathVariable Long id){
        return authorService.get(id);
    }

    @GetMapping()
    public List<Author> getAuthor(@RequestParam(value = "name", required = false) String name) {
        return authorService.getAuthorData(name);
    }
    @PutMapping("/{id}")
    public AuthorResponse updateBook(@PathVariable Long id,
                                   @RequestBody AuthorUpdateRequest updateRequest){
        return authorService.update(updateRequest,id);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id){
        authorService.delete(id);
    }
}
