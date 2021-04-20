package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.common.threads.Multithreading;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.FileStorageEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class CSVReaderService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private final FileStorageRepository fileStorageRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final Path fileStorageLocation;
    private final RoleRepository roleRepository;
    private final Multithreading multithreading;

    public CSVReaderService(AuthorRepository authorRepository, PublisherRepository publisherRepository, GenreRepository genreRepository, UserRepository userRepository, UserDetailsRepository userDetailsRepository, FileStorageEntity fileStorageEntity, FileStorageRepository fileStorageRepository, BookRepository bookRepository, CommunityRepository communityRepository, RoleRepository roleRepository, Multithreading multithreading) {
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.fileStorageLocation = Paths.get(fileStorageEntity.getUploadDir()).toAbsolutePath().normalize();
        this.bookRepository = bookRepository;
        this.communityRepository = communityRepository;
        this.roleRepository = roleRepository;
        this.multithreading = multithreading;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void csvAssignGenreToBook(MultipartFile multipartFile) {
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {
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
