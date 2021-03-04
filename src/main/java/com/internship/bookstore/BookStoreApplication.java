package com.internship.bookstore;

import com.internship.bookstore.entity.genre.Genre;
import com.internship.bookstore.persistence.genre.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BookStoreApplication implements CommandLineRunner {

    @Autowired
    private GenreRepository genreRepository;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Genre classic=new Genre("classic");
        Genre historical=new Genre("historical");
        Genre fantasy=new Genre("fantasy");
        genreRepository.saveAll(List.of(classic,historical,fantasy));
    }
}
