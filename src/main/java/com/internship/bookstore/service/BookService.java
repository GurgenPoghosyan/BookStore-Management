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
import com.internship.bookstore.persistence.repository.PublisherRepository;
import com.internship.bookstore.service.criteria.BookSearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public BookDto create(BookDto bookDto) {
        BookEntity bookEntity = BookDto.mapDtoToEntity(bookDto);
        bookEntity.setPublisher(publisherRepository.findByName(bookDto.getPublisher().getName()));
        List<AuthorDto> listOfAuthors = bookDto.getAuthors();
        for (AuthorDto authorDto: listOfAuthors){
            AuthorEntity authorEntity = authorRepository.findByName(authorDto.getName());
            if (authorEntity!=null){
                bookEntity.getAuthors().add(authorEntity);
            }else{
                AuthorEntity savedAuthor = authorRepository.save(AuthorDto.mapDtoToEntity(authorDto));
                bookEntity.getAuthors().add(savedAuthor);
            }
        }
        List<GenreDto> listOfGenres = bookDto.getGenres();
        for (GenreDto genreDto: listOfGenres){
            GenreEntity genreEntity = genreRepository.findByGenreName(genreDto.getName());
            if (genreEntity!=null){
                bookEntity.getGenres().add(genreEntity);
            }else{
                GenreEntity savedGenre = genreRepository.save(GenreDto.mapDtoToEntity(genreDto));
                bookEntity.getGenres().add(savedGenre);
            }
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
                                                            criteria.getMinPages(),
                                                            criteria.getMaxPages(),
                                                            criteria.composePageRequest());
        Page<BookDto> dtoContent = entityContent.map(BookDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public BookDto update(BookDto bookDto, Long id) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        BookEntity mapped = BookDto.mapDtoToEntity(bookDto);
        mapped.setPublisher(publisherRepository.findByName(bookDto.getPublisher().getName()));
        List<AuthorDto> listOfAuthors = bookDto.getAuthors();
        for (AuthorDto authorDto: listOfAuthors){
            AuthorEntity authorEntity = authorRepository.findByName(authorDto.getName());
            if (authorEntity!=null){
                mapped.getAuthors().add(authorEntity);
            }else{
                AuthorEntity savedAuthor = authorRepository.save(AuthorDto.mapDtoToEntity(authorDto));
                mapped.getAuthors().add(savedAuthor);
            }
        }
        List<GenreDto> listOfGenres = bookDto.getGenres();
        for (GenreDto genreDto: listOfGenres){
            GenreEntity genreEntity = genreRepository.findByGenreName(genreDto.getName());
            if (genreEntity!=null){
                mapped.getGenres().add(genreEntity);
            }else{
                GenreEntity savedGenre = genreRepository.save(GenreDto.mapDtoToEntity(genreDto));
                mapped.getGenres().add(savedGenre);
            }
        }
        mapped.setId(bookEntity.getId());
        BookEntity updatedBookEntity = bookRepository.save(mapped);
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
}
