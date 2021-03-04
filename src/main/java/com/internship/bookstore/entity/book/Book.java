package com.internship.bookstore.entity.book;

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

    @Column(name = "genre")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Genre> genres;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    public void addAuthorToBook(Author author){
        if (authors==null){
            authors=new ArrayList<>();
        }
        authors.add(author);
    }

    public void addGenreToBook(Genre genre){
        if (genres==null){
            genres=new ArrayList<>();
        }
        genres.add(genre);
    }


}
