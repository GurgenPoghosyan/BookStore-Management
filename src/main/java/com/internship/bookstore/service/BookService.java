package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.PublisherNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.persistence.repository.PublisherRepository;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.PublisherDto;
import com.internship.bookstore.service.model.BookWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class BookService implements CRUDService<BookDto, BookDto, BookDto, Long> {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Autowired
    public BookService(BookRepository bookRepository,
                       GenreRepository genreRepository,
                       AuthorRepository authorRepository,
                       PublisherRepository publisherRepository,
                       AuthorService authorService,
                       GenreService genreService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public BookDto create(BookDto bookDto) {
        BookEntity bookEntity = mapDtoToEntity(bookDto);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        return mapEntityToDto(savedBookEntity);
    }

    @Override
    public BookDto get(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return mapEntityToDto(bookEntity);
    }

    public List<BookWrapper> getBookData(String name) {
        if (name != null) {
            return bookRepository.findAllBooks().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return bookRepository.findAllBooks();
    }

    @Override
    public void delete(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(bookEntity);
    }

    @Override
    public BookDto update(BookDto bookDto, Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookEntity.setName(bookDto.getName());
        bookEntity.setDate(bookDto.getDate());
        BookEntity updatedBookEntity = bookRepository.save(bookEntity);
        return mapEntityToDto(updatedBookEntity);
    }

    public List<AuthorDto> getBookAuthors(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookDto dto = mapEntityToDto(bookEntity);
        List<Long> authors = dto.getAuthors();
        return authorService.mapLongListToEntityList(authors).stream().map(AuthorService::mapEntityToDto).collect(Collectors.toList());
    }

    public BookDto addAuthorToBook(Long bookId, Long authorId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        AuthorEntity authorEntity = authorRepository.findById(authorId).
                orElseThrow(() -> new AuthorNotFoundException(authorId));
        bookEntity.getAuthors().add(authorEntity);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        return mapEntityToDto(savedBookEntity);
    }

    public BookEntity mapDtoToEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName(bookDto.getName());
        bookEntity.setLanguage(bookDto.getLanguage());
        bookEntity.setPages(bookDto.getPages());
        bookEntity.setDate(bookDto.getDate());
        bookEntity.setRating(bookDto.getRating());
        bookEntity.setPublisher(publisherRepository.findById(bookDto.getPublisherId()).
                orElseThrow(()->new PublisherNotFoundException(bookDto.getPublisherId())));
        List<Long> listOfAuthors = bookDto.getAuthors();
        bookEntity.setAuthors(authorService.mapLongListToEntityList(listOfAuthors));
        List<Long> listOfGenres = bookDto.getGenres();
        bookEntity.setGenres(genreService.mapLongListToEntityList(listOfGenres));
        return bookEntity;
    }
    public static BookDto mapEntityToDto(BookEntity entity) {
        if (entity == null) {
            return null;
        }
        BookDto dto = new BookDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        dto.setPages(entity.getPages());
        dto.setLanguage(entity.getLanguage());
        dto.setRating(entity.getRating());
        dto.setPublisherId(entity.getPublisher().getId());
        List<AuthorEntity> listOfAuthors = entity.getAuthors();
        if (!CollectionUtils.isEmpty(listOfAuthors)) {
            dto.setAuthors(listOfAuthors.stream().map(AuthorEntity::getId).collect(Collectors.toList()));
        }
        List<GenreEntity> listOfGenres = entity.getGenres();
        if (!CollectionUtils.isEmpty(listOfGenres)) {
            dto.setGenres(listOfGenres.stream().map(GenreEntity::getId).collect(Collectors.toList()));
        }

        return dto;
    }
}