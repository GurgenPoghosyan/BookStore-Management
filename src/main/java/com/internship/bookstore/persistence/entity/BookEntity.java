package com.internship.bookstore.persistence.entity;

import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.dto.BookDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "book_name")
    private String name;

    @Column(name = "date_of_publication")
    private String date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AuthorEntity> authors;

    public static BookEntity mapDtoToEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName(bookDto.getName());
        List<AuthorDto> listOfAuthors = bookDto.getAuthors();
        bookEntity.setAuthors(listOfAuthors.stream().map(AuthorEntity::mapDtoToEntity).collect(Collectors.toList()));
        return bookEntity;
    }

}
