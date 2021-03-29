package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.service.criteria.GenreSearchCriteria;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreEntity> mapLongListToEntityList(List<Long> genres) {
        List<GenreEntity> list = new ArrayList<>();
        for (Long longId : genres) {
            GenreEntity byId = genreRepository.findById(longId).orElseThrow(() -> new GenreNotFoundException(longId));
            list.add(byId);
        }
        return list;
    }

    public GenreDto create(GenreDto genreDto) {
        GenreEntity genreEntity = GenreDto.mapDtoToEntity(genreDto);
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    public GenreDto getGenre(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        return GenreDto.mapEntityToDto(genreEntity);
    }

    public QueryResponseWrapper<GenreDto> getGenres(GenreSearchCriteria criteria) {
        Page<GenreEntity> contentEntity = genreRepository.find(criteria.getName(),criteria.composePageRequest());
        Page<GenreDto> contentDto = contentEntity.map(GenreDto::mapEntityToDto);
        return new QueryResponseWrapper<>(contentDto.getTotalElements(), contentDto.getContent());
    }

    public GenreDto update(GenreDto genreDto, Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        GenreEntity mapped = GenreDto.mapDtoToEntity(genreDto);
        mapped.setId(genreEntity.getId());
        GenreEntity savedGenre = genreRepository.save(mapped);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    public void delete(Long id) {
        genreRepository.deleteById(id);
    }
}
