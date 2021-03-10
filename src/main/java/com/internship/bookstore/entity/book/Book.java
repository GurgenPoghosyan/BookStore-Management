package com.internship.bookstore.entity.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.entity.genre.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_publication")
    private String date;

    @ManyToMany()
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany()
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnore
    private List<Author> authors = new ArrayList<>();
}
