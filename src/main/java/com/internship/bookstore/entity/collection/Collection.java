package com.internship.bookstore.entity.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.internship.bookstore.entity.book.Book;
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
@Table(name = "collections")
@Getter
@Setter
@NoArgsConstructor
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "collection_name")
    private String name;

    @OneToMany
    private List<Book> books = new ArrayList<>();

    @OneToOne
    private Genre genre;
}
