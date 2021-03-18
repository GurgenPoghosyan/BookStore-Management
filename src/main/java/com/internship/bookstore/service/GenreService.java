package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.service.dto.GenreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public static GenreDto mapEntityToDto(GenreEntity genreEntity) {
        if (genreEntity == null) {
            return null;
        }
        GenreDto dto = new GenreDto();
        dto.setId(genreEntity.getId());
        dto.setGenreName(genreEntity.getGenreName());
        return dto;
    }

    public static GenreEntity mapDtoToEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }
        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setGenreName(genreDto.getGenreName());
        return genreEntity;
    }

    public List<GenreEntity> mapLongListToEntityList(List<Long> genres) {
        List<GenreEntity> list = new ArrayList<>();
        for (Long longId : genres) {
            GenreEntity byId = genreRepository.findById(longId).orElseThrow(() -> new GenreNotFoundException(longId));
            list.add(byId);
        }
        return list;
    }
}
