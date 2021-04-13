package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.persistence.entity.*;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.persistence.repository.PublisherRepository;
import com.internship.bookstore.security.session.SessionUser;
import com.internship.bookstore.service.criteria.BookSearchCriteria;
import com.internship.bookstore.service.dto.*;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final FileStorageService fileStorageService;

    public BookDto create(BookDto bookDto) {
        BookEntity bookEntity = BookDto.mapDtoToEntity(bookDto);
        PublisherDto publisherDto = bookDto.getPublisher();
        PublisherEntity publisherEntity = publisherRepository.findByName(publisherDto.getName());
        if (publisherEntity == null) {
            publisherEntity = publisherRepository.save(PublisherDto.mapDtoToEntity(publisherDto));
        }
        bookEntity.setPublisher(publisherEntity);
        List<AuthorDto> listOfAuthors = bookDto.getAuthors();
        for (AuthorDto authorDto : listOfAuthors) {
            AuthorEntity authorEntity = authorRepository.findByName(authorDto.getName());
            if (authorEntity == null) {
                authorEntity = authorRepository.save(AuthorDto.mapDtoToEntity(authorDto));
            }
            bookEntity.getAuthors().add(authorEntity);
        }

        List<GenreDto> listOfGenres = bookDto.getGenres();
        for (GenreDto genreDto : listOfGenres) {
            GenreEntity genreEntity = genreRepository.findByGenreName(genreDto.getName());
            if (genreEntity == null) {
                genreEntity = genreRepository.save(GenreDto.mapDtoToEntity(genreDto));
            }
            bookEntity.getGenres().add(genreEntity);
        }
        BookEntity savedBookEntity = bookRepository.save(bookEntity);
        return BookDto.mapEntityToDto(savedBookEntity);
    }

    public BookDto getBook(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return BookDto.mapEntityToDto(bookEntity);
    }

    public QueryResponseWrapper<BookDto> getBooks(BookSearchCriteria criteria) {
        Page<BookEntity> entityContent = bookRepository.find(criteria.getName(),
                                                            criteria.getMinRating(),
                                                            criteria.getStartDate(),
                                                            criteria.getEndDate(),
                                                            criteria.getMinPageSize(),
                                                            criteria.getMaxPageSize(),
                                                            criteria.getAuthorName(),
                                                            criteria.getGenreName(),
                                                            criteria.getPublisherName(),
                                                            criteria.composePageRequest());
        Page<BookDto> dtoContent = entityContent.map(BookDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public BookDto update(BookDto bookDto, Long id) {
        if (bookDto.getName() == null) {
            throw new NullPointerException("Book name is required");
        }
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        PublisherDto publisherDto = bookDto.getPublisher();
        if (publisherDto != null) {
            PublisherEntity publisherEntity = publisherRepository.findByName(publisherDto.getName());
            if (publisherEntity == null) {
                publisherEntity = publisherRepository.save(PublisherDto.mapDtoToEntity(publisherDto));
            }
            bookEntity.setPublisher(publisherEntity);
        }

        List<AuthorDto> listOfAuthors = bookDto.getAuthors();
        if (listOfAuthors != null) {
            List<AuthorEntity> listOfAuthorEntities = new ArrayList<>();
            for (AuthorDto authorDto : listOfAuthors) {
                AuthorEntity authorEntity = authorRepository.findByName(authorDto.getName());
                if (authorEntity == null) {
                    authorEntity = authorRepository.save(AuthorDto.mapDtoToEntity(authorDto));
                }
                listOfAuthorEntities.add(authorEntity);
            }
            bookEntity.setAuthors(listOfAuthorEntities);
        }

        List<GenreDto> listOfGenres = bookDto.getGenres();
        if (listOfGenres != null) {
            List<GenreEntity> listOfGenreEntities = new ArrayList<>();
            for (GenreDto genreDto : listOfGenres) {
                GenreEntity genreEntity = genreRepository.findByGenreName(genreDto.getName());
                if (genreEntity == null) {
                    genreEntity = genreRepository.save(GenreDto.mapDtoToEntity(genreDto));
                }
                listOfGenreEntities.add(genreEntity);
            }
            bookEntity.setGenres(listOfGenreEntities);
        }
        bookEntity.setName(bookDto.getName());
        if (bookDto.getIsbn() != null) {
            bookEntity.setIsbn(bookDto.getIsbn());
        }
        if (bookDto.getDate() != null) {
            bookEntity.setDate(bookDto.getDate());
        }
        if (bookDto.getPages() != null) {
            bookEntity.setPages(bookEntity.getPages());
        }
        if (bookDto.getRating() != null) {
            bookEntity.setRating(bookEntity.getRating());
        }
        BookEntity updatedBookEntity = bookRepository.save(bookEntity);
        return BookDto.mapEntityToDto(updatedBookEntity);
    }

    public List<AuthorDto> getBooksAuthors(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookDto dto = BookDto.mapEntityToDto(bookEntity);
        return dto.getAuthors();
    }

    public List<GenreDto> getBooksGenres(Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookDto dto = BookDto.mapEntityToDto(bookEntity);
        return dto.getGenres();
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

    public FileStorageDto uploadFile(MultipartFile multipartFile, Long bookId){
        FileStorageEntity fileStorageEntity = new FileStorageEntity();
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + extension;
        fileStorageEntity.setFileName(fileName);
        fileStorageEntity.setExtension(extension);
        FileStorageEntity savedFile = fileStorageService.storeFile(multipartFile, fileStorageEntity);

        BookEntity bookEntity = bookRepository.findById(bookId).
                orElseThrow(() -> new BookNotFoundException(bookId));
        bookEntity.setBookCoverImage(savedFile);
        bookRepository.save(bookEntity);
        return FileStorageDto.mapEntityToDto(savedFile);
    }

    public void saveBooks(MultipartFile multipartFile) {

    }
}
