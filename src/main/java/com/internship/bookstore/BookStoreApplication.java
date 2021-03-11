package com.internship.bookstore;

import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.GenreRepository;
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
        GenreEntity classic=new GenreEntity("classic");
        GenreEntity historical=new GenreEntity("historical");
        GenreEntity fantasy=new GenreEntity("fantasy");
        genreRepository.saveAll(List.of(classic,historical,fantasy));
    }
}
