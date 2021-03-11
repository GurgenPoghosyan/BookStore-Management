package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.service.dto.BookDto;
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
public class BookService implements CRUDService<BookCreateRequest,BookUpdateRequest,BookDto,Long> {

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
    public BookDto create(BookCreateRequest createRequest) {
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(createRequest, bookEntity);
        GenreEntity genreEntity = genreRepository.findByGenreName(createRequest.getGenreName()).
                orElseThrow(() -> new GenreNotFoundException(createRequest.getGenreName()));
        AuthorEntity authorEntity = authorRepository.findById(createRequest.getAuthorId()).
                orElseThrow(() -> new AuthorNotFoundException(createRequest.getAuthorId()));
        bookEntity.getGenres().add(genreEntity);
        bookEntity.getAuthors().add(authorEntity);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        return BookDto.mapEntityToDto(savedBookEntity);
    }

    @Override
    public BookDto get(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return BookDto.mapEntityToDto(bookEntity);
    }

    public List<BookEntity> getBookData(String name) {
        if (name != null) {
            return bookRepository.findAll().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return bookRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(bookEntity);
    }

    @Override
    public BookDto update(BookUpdateRequest updateRequest, Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BeanUtils.copyProperties(updateRequest, bookEntity);
        BookEntity updatedBookEntity = bookRepository.save(bookEntity);
        return BookDto.mapEntityToDto(updatedBookEntity);
    }

    public List<String> getBookAuthors(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookDto dto = BookDto.mapEntityToDto(bookEntity);
        return dto.getAuthors();
    }

    public ResponseEntity<BookDto> addAuthorToBook(Long bookId, Long authorId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        AuthorEntity authorEntity = authorRepository.findById(authorId).
                orElseThrow(() -> new AuthorNotFoundException(authorId));
        bookEntity.getAuthors().add(authorEntity);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        BookDto bookDto = new BookDto();
        BeanUtils.copyProperties(savedBookEntity,bookDto);
        return ResponseEntity.ok(bookDto);
    }
}
