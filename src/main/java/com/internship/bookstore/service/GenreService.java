package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.service.criteria.GenreSearchCriteria;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreDto create(GenreDto genreDto) {
        if (genreDto.getName() == null) {
            throw new NullPointerException("Genre name is required");
        }
        GenreEntity genreEntity = GenreDto.mapDtoToEntity(genreDto);
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    public GenreDto getGenre(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        return GenreDto.mapEntityToDto(genreEntity);
    }

    public QueryResponseWrapper<GenreDto> getGenres(GenreSearchCriteria criteria) {
        Page<GenreEntity> contentEntity = genreRepository.find(criteria.getName(), criteria.composePageRequest());
        Page<GenreDto> contentDto = contentEntity.map(GenreDto::mapEntityToDto);
        return new QueryResponseWrapper<>(contentDto.getTotalElements(), contentDto.getContent());
    }

    public GenreDto update(GenreDto genreDto, Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        if (genreDto.getName() != null) {
            genreEntity.setGenreName(genreDto.getName());
        }
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    public void delete(Long id) {
        genreRepository.deleteById(id);
    }
}
