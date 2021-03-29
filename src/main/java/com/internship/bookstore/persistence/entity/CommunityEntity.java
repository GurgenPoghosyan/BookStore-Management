package com.internship.bookstore.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Gurgen Poghosyan
 */
@Data
@Entity
@Table(name = "community")
@NoArgsConstructor
public class CommunityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "zip_code", nullable = false)
    private String zip;
}
