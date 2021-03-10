package com.internship.bookstore.service.author;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.book.Book;
import com.internship.bookstore.persistence.author.AuthorRepository;
import com.internship.bookstore.service.book.CRUDService;
import com.internship.bookstore.transform.request.author.AuthorCreateRequest;
import com.internship.bookstore.transform.request.author.AuthorUpdateRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Gurgen Poghosyan
 */
@Service
public class AuthorService implements CRUDService<AuthorCreateRequest, AuthorUpdateRequest,ResponseEntity<Author>,Long> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public ResponseEntity<Author> create(AuthorCreateRequest createRequest) {
        Author author = new Author();
        BeanUtils.copyProperties(createRequest, author);
        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @Override
    public ResponseEntity<Author> get(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        return ResponseEntity.ok(author);
    }

    public List<Author> getAuthorData(String name) {
        if (name != null) {
            return authorRepository.findAll().stream().filter(author -> author.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return authorRepository.findAll();
    }

    @Override
    public ResponseEntity<Author> update(AuthorUpdateRequest updateRequest, Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        BeanUtils.copyProperties(updateRequest, author);
        Author updatedAuthor = authorRepository.save(author);
        return ResponseEntity.ok(updatedAuthor);
    }

    @Override
    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(author);
    }

    public List<Book> getAuthorBooks(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        return author.getBooks();
    }
}
