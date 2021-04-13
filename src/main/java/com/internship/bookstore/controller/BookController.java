package com.internship.bookstore.controller;

import com.internship.bookstore.security.session.SessionUser;
import com.internship.bookstore.service.BookService;
import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.criteria.BookSearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.FileStorageDto;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import com.internship.bookstore.transform.requestbody.book.AddAuthorRequestBody;
import com.internship.bookstore.transform.requestbody.book.AddGenreRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.internship.bookstore.security.session.SessionUser.SESSION_USER_KEY;

/**
 * @author Gurgen Poghosyan
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final CSVReaderService csvReaderService;

    @PostMapping
    @PreAuthorize("hasAuthority('EDITOR')")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        if (bookDto.getName() == null) {
            throw new NullPointerException("Book name is required");
        }
        if (bookDto.getAuthors() == null) {
            throw new NullPointerException("Authors list is required");
        }
        if (bookDto.getGenres() == null) {
            throw new NullPointerException("Genres list is required");
        }
        BookDto dto = bookService.create(bookDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('EDITOR','ADMIN')")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        BookDto dto = bookService.getBook(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','EDITOR','USER')")
    public QueryResponseWrapper<BookDto> getBooks(BookSearchCriteria criteria) {
        return bookService.getBooks(criteria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDITOR')")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                              @RequestBody BookDto bookDto) {
        BookDto dto = bookService.update(bookDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/{id}/authors")
    @PreAuthorize("hasAnyAuthority('EDITOR','USER')")
    public List<AuthorDto> getBooksAuthors(@PathVariable Long id) {

        return bookService.getBooksAuthors(id);
    }

    @GetMapping("/{id}/genres")
    @PreAuthorize("hasAnyAuthority('EDITOR','USER')")
    public List<GenreDto> getBooksGenres(@PathVariable Long id) {
        return bookService.getBooksGenres(id);
    }

    @PostMapping("/{id}/authors")
    @PreAuthorize("hasAuthority('EDITOR')")
    public ResponseEntity<BookDto> addAuthorToBook(@PathVariable Long id, @RequestBody AddAuthorRequestBody requestBody) {
        BookDto dto = bookService.addAuthorToBook(id, requestBody.getAuthorId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/genres")
    @PreAuthorize("hasAuthority('EDITOR')")
    public ResponseEntity<BookDto> addGenreToBook(@PathVariable Long id, @RequestBody AddGenreRequestBody requestBody) {
        BookDto dto = bookService.addGenreToBook(id, requestBody.getGenreId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("{id}/files/upload")
    @PreAuthorize("hasAnyAuthority('EDITOR')")
    public ResponseEntity<FileStorageDto> uploadFile(@RequestParam("image") MultipartFile file,
                                                     @PathVariable Long id) {
        FileStorageDto fileStorageDto = bookService.uploadFile(file,id);
        return ResponseEntity.ok(fileStorageDto);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void uploadBooks(@RequestParam("books") MultipartFile multipartFile) {
        csvReaderService.csvBooksProcessor(multipartFile);
    }

//    @PostMapping("/upload")
//    public void uploadBooks(@RequestParam("books") MultipartFile multipartFile) {
//        bookService.saveBooks(multipartFile);
//    }

    @PostMapping("/genres/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void uploadGenres(@RequestParam("genre-to-book") MultipartFile multipartFile) {
        csvReaderService.csvAssignGenreToBook(multipartFile);
    }
}
