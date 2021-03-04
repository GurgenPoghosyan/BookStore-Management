package com.internship.bookstore.service.book;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.book.Book;
import com.internship.bookstore.entity.genre.Genre;
import com.internship.bookstore.persistence.author.AuthorRepository;
import com.internship.bookstore.persistence.book.BookRepository;
import com.internship.bookstore.persistence.genre.GenreRepository;
import com.internship.bookstore.transform.request.author.AuthorCreateRequest;
import com.internship.bookstore.transform.request.book.BookCreateRequest;
import com.internship.bookstore.transform.request.book.BookUpdateRequest;
import com.internship.bookstore.transform.response.author.AuthorResponse;
import com.internship.bookstore.transform.response.book.BookResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class BookService implements CreateSupported<BookCreateRequest, BookResponse>,
        GetSupported<Long, BookResponse>,
        UpdateSupported<BookResponse, BookUpdateRequest, Long>,
        DeleteSupported<Long> {

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
    public BookResponse create(BookCreateRequest createRequest) {
        Book book = new Book();
        BeanUtils.copyProperties(createRequest, book);
        Genre genre=genreRepository.findByGenreName(createRequest.getGenreName()).
                orElseThrow(()->new GenreNotFoundException(createRequest.getGenreName()));
        book.addGenreToBook(genre);
        Book savedBook = bookRepository.save(book);
        BookResponse response = new BookResponse();
        BeanUtils.copyProperties(savedBook, response);
        return response;
    }

    @Override
    public BookResponse get(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookResponse response = new BookResponse();
        BeanUtils.copyProperties(book, response);
        return response;
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
    public BookResponse update(BookUpdateRequest updateRequest, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BeanUtils.copyProperties(updateRequest, book);
        Book updatedBook = bookRepository.save(book);
        BookResponse response = new BookResponse();
        BeanUtils.copyProperties(updatedBook, response);
        return response;
    }
}
