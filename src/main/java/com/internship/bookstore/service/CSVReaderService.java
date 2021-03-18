package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.entity.PublisherEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.persistence.repository.PublisherRepository;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.GenreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class CSVReaderService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public CSVReaderService(AuthorRepository authorRepository,
                            BookRepository bookRepository,
                            PublisherRepository publisherRepository,
                            GenreRepository genreRepository,
                            AuthorService authorService, BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    public List<BookDto> csvBooksProcessor() {

        String csvFile = "C:/Users/pogho/Desktop/books.csv";
        String line = "";
        String cvsSplitBy = ",";
        List<BookDto> books = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                BookEntity bookEntity = new BookEntity();
                String[] dataPoints = line.split(cvsSplitBy);
                AuthorDto authorDto = new AuthorDto();
                String[] splitAuthors = dataPoints[2].split("/");
                for (String s : splitAuthors) {
                    authorDto.setName(s);
                    AuthorEntity byName = authorRepository.findByName(s);
                    if (byName != null) {
                        bookEntity.getAuthors().add(byName);
                        continue;
                    }
                    AuthorEntity authorEntity = authorService.mapDtoToEntity(authorDto);
                    AuthorEntity savedAuthor = authorRepository.save(authorEntity);
                    bookEntity.getAuthors().add(savedAuthor);
                }
                PublisherEntity publisherEntity = publisherRepository.findByName(dataPoints[11].split(";")[0]);
                if (publisherEntity != null) {
                    bookEntity.setPublisher(publisherEntity);
                } else {
                    PublisherEntity entity = new PublisherEntity();
                    entity.setName(dataPoints[11].split(";")[0]);
                    PublisherEntity savedPublisher = publisherRepository.save(entity);
                    bookEntity.setPublisher(savedPublisher);
                }
                bookEntity.setName(dataPoints[1]);
                bookEntity.setPages(Integer.parseInt(dataPoints[7]));
                bookEntity.setDate(dataPoints[10]);
                bookEntity.setRating(Double.parseDouble(dataPoints[3]));
                bookEntity.setLanguage(dataPoints[6]);
                BookEntity savedBook = bookRepository.save(bookEntity);
                books.add(bookService.mapEntityToDto(savedBook));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<GenreDto> csvGenreProcessor() {

        File csvFile = new File("C:/Users/pogho/Desktop/Book reviews/Preprocessed_data.csv");
        String line = "";
        String nextLine;
        List<GenreDto> genres = new ArrayList<>();

        try (Scanner s = new Scanner(csvFile)) {
            s.nextLine();
            long i = 0L;
            line = s.nextLine();
            String[] values = new String[0];
            while (s.hasNextLine()) {
                nextLine = s.nextLine();
                int k = 0;
                if (Character.isDigit(nextLine.charAt(0))) {
                    k = 0;
                    while (Character.isDigit(nextLine.charAt(k))) {
                        k++;
                    }
                }
                if (k == String.valueOf(i).length()) {
                    line = nextLine;
                } else {
                    while (!line.startsWith(String.valueOf(i + 1))) {
                        String[] values2 = nextLine.split("\"");
                        if (values2.length > 1) {
                            values = nextLine.split("'");
                            if (values.length > 1) {
                                if (values.length == 2) {
                                    values = nextLine.split("\"\"");
                                }
                                System.out.println(i + " " + Arrays.toString(values));
                                GenreEntity entity = genreRepository.findByGenreName(values[1]);
                                if (entity == null) {
                                    GenreEntity genreEntity = new GenreEntity();
                                    genreEntity.setGenreName(values[1]);
                                    GenreEntity savedGenre = genreRepository.save(genreEntity);
                                    genres.add(GenreService.mapEntityToDto(savedGenre));
                                }
                            }
                            line = s.nextLine();
                        } else {
                            nextLine = s.nextLine();
                        }
                    }
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return genres;
    }

    public void csvAssignGenreToBook() {
        String csvFile = "C:/Users/pogho/Desktop/book-genres.csv";
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                BookEntity bookEntity = bookRepository.findById(Long.parseLong(values[0])).orElseThrow(() -> new BookNotFoundException(Long.parseLong(values[0])));
                GenreEntity genreEntity = genreRepository.findById(Long.parseLong(values[1])).orElseThrow(() -> new GenreNotFoundException(Long.parseLong(values[1])));
                bookEntity.getGenres().add(genreEntity);
                bookRepository.save(bookEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
