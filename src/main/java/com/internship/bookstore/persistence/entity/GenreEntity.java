package com.internship.bookstore.persistence.entity;

import com.internship.bookstore.service.dto.GenreDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Gurgen Poghosyan
 */
@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "genre")
    private String genreName;

    public GenreEntity(String genreName) {
        this.genreName = genreName;
    }

    public static GenreEntity mapDtoToEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }
        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setGenreName(genreDto.getGenreName());
        return genreEntity;
    }
}
