package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.common.exceptions.PublisherNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.entity.PublisherEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.persistence.repository.PublisherRepository;
import com.internship.bookstore.service.criteria.SearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.BookWrapper;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class BookService implements CRUDService<BookDto> {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Autowired
    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       GenreRepository genreRepository,
                       PublisherRepository publisherRepository,
                       AuthorService authorService,
                       GenreService genreService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }


    @Override
    public BookDto create(BookDto bookDto) {
        BookEntity bookEntity = BookDto.mapDtoToEntity(bookDto);
        bookEntity.setPublisher(publisherRepository.findById(bookDto.getPublisherId()).
                orElseThrow(() -> new PublisherNotFoundException(bookDto.getPublisherId())));
        List<Long> listOfAuthors = bookDto.getAuthors();
        bookEntity.setAuthors(authorService.mapLongListToEntityList(listOfAuthors));
        List<Long> listOfGenres = bookDto.getGenres();
        bookEntity.setGenres(genreService.mapLongListToEntityList(listOfGenres));
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        return BookDto.mapEntityToDto(savedBookEntity);
    }

    @Override
    public BookDto get(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return BookDto.mapEntityToDto(bookEntity);
    }

    public List<BookWrapper> getBooks(String name) {
        if (name != null) {
            return bookRepository.findAllBooks().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return bookRepository.findAllBooks();
    }

    public QueryResponseWrapper<BookWrapper> getBooks(SearchCriteria criteria) {
        Page<BookWrapper> content = bookRepository.findAllWithPagination(criteria.composePageRequest());
        return new QueryResponseWrapper<>(content.getTotalElements(), content.getContent());
    }

    @Override
    public void delete(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(bookEntity);
    }

    @Override
    public BookDto update(BookDto bookDto, Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookEntity mapped = BookDto.mapDtoToEntity(bookDto);
        mapped.setPublisher(publisherRepository.findById(bookDto.getPublisherId()).
                orElseThrow(() -> new PublisherNotFoundException(bookDto.getPublisherId())));
        List<Long> listOfAuthors = bookDto.getAuthors();
        mapped.setAuthors(authorService.mapLongListToEntityList(listOfAuthors));
        List<Long> listOfGenres = bookDto.getGenres();
        mapped.setGenres(genreService.mapLongListToEntityList(listOfGenres));
        mapped.setId(bookEntity.getId());
        BookEntity updatedBookEntity = bookRepository.save(mapped);
        return BookDto.mapEntityToDto(updatedBookEntity);
    }

    public List<AuthorDto> getBooksAuthors(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookDto dto = BookDto.mapEntityToDto(bookEntity);
        List<Long> authors = dto.getAuthors();
        return authorService.mapLongListToEntityList(authors).stream().map(AuthorDto::mapEntityToDto).collect(Collectors.toList());
    }

    public List<GenreDto> getBooksGenres(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookDto dto = BookDto.mapEntityToDto(bookEntity);
        List<Long> genres = dto.getGenres();
        return genreService.mapLongListToEntityList(genres).stream().map(GenreDto::mapEntityToDto).collect(Collectors.toList());
    }

    public BookDto addAuthorToBook(Long bookId, Long authorId) {
        BookEntity bookEntity = bookRepository.findById(bookId).
                orElseThrow(() -> new BookNotFoundException(bookId));
        AuthorEntity authorEntity = authorRepository.findById(authorId).
                orElseThrow(() -> new AuthorNotFoundException(authorId));
        bookEntity.getAuthors().add(authorEntity);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        return BookDto.mapEntityToDto(savedBookEntity);
    }

    public BookDto addGenreToBook(Long bookId, Long genreId) {
        BookEntity bookEntity = bookRepository.findById(bookId).
                orElseThrow(() -> new BookNotFoundException(bookId));
        GenreEntity genreEntity = genreRepository.findById(genreId).
                orElseThrow(() -> new GenreNotFoundException(genreId));
        bookEntity.getGenres().add(genreEntity);
        BookEntity savedBook = bookRepository.save(bookEntity);
        return BookDto.mapEntityToDto(savedBook);
    }

    public List<BookEntity> mapLongListToEntityList(List<Long> listOfBooks) {
        List<BookEntity> list = new ArrayList<>();
        for (Long book : listOfBooks) {
            BookEntity bookEntity = bookRepository.findById(book).orElseThrow(() -> new AuthorNotFoundException(book));
            list.add(bookEntity);
        }
        return list;
    }
}
