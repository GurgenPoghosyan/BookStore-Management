package com.internship.bookstore.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "author_name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "authors")
    private List<BookEntity> books;
}
