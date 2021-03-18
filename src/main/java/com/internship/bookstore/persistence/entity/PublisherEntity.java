package com.internship.bookstore.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Gurgen Poghosyan
 */
@Entity
@Table(name = "publishers")
@Getter
@Setter
@NoArgsConstructor
public class PublisherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "publisher_name")
    private String name;

}
