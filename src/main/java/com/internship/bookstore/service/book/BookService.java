package com.internship.bookstore.service.book;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.book.Book;
import com.internship.bookstore.entity.genre.Genre;
import com.internship.bookstore.persistence.author.AuthorRepository;
import com.internship.bookstore.persistence.book.BookRepository;
import com.internship.bookstore.persistence.genre.GenreRepository;
import com.internship.bookstore.transform.request.book.BookCreateRequest;
import com.internship.bookstore.transform.request.book.BookUpdateRequest;
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
public class BookService implements CRUDService<BookCreateRequest,BookUpdateRequest,ResponseEntity<Book>,Long> {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository,
                       GenreRepository genreRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public ResponseEntity<Book> create(BookCreateRequest createRequest) {
        Book book = new Book();
        BeanUtils.copyProperties(createRequest, book);
        Genre genre = genreRepository.findByGenreName(createRequest.getGenreName()).
                orElseThrow(() -> new GenreNotFoundException(createRequest.getGenreName()));
        Author author = authorRepository.findById(createRequest.getAuthorId()).
                orElseThrow(() -> new AuthorNotFoundException(createRequest.getAuthorId()));
        book.getGenres().add(genre);
        book.getAuthors().add(author);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Override
    public ResponseEntity<Book> get(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return ResponseEntity.ok(book);
    }

    public List<Book> getBookData(String name) {
        if (name != null) {
            return bookRepository.findAll().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return bookRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

    @Override
    public ResponseEntity<Book> update(BookUpdateRequest updateRequest, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BeanUtils.copyProperties(updateRequest, book);
        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    public List<Author> getBookAuthors(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return book.getAuthors();
    }

    public ResponseEntity<Book> addAuthorToBook(Long bookId, Long authorId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        Author author = authorRepository.findById(authorId).
                orElseThrow(() -> new AuthorNotFoundException(authorId));
        book.getAuthors().add(author);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(savedBook);
    }
}
