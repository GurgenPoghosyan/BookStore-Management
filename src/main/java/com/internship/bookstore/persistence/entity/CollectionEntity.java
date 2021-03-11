package com.internship.bookstore.persistence.entity;

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
public class CollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "collection_name")
    private String name;

    @OneToMany
    private List<BookEntity> bookEntities = new ArrayList<>();

    @OneToOne
    private GenreEntity genreEntity;
}
