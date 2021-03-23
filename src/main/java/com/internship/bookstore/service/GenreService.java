package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.service.criteria.SearchCriteria;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class GenreService implements CRUDService<GenreDto> {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }



    public List<GenreEntity> mapLongListToEntityList(List<Long> genres) {
        List<GenreEntity> list = new ArrayList<>();
        for (Long longId : genres) {
            GenreEntity byId = genreRepository.findById(longId).orElseThrow(() -> new GenreNotFoundException(longId));
            list.add(byId);
        }
        return list;
    }

    @Override
    public GenreDto create(GenreDto genreDto) {
        GenreEntity genreEntity = GenreDto.mapDtoToEntity(genreDto);
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    public List<GenreDto> getGenres(String name) {
        if (name != null) {
            return genreRepository.findAll().stream().filter(genre -> genre.getGenreName().equalsIgnoreCase(name)).collect(Collectors.toList())
                    .stream().map(GenreDto::mapEntityToDto).collect(Collectors.toList());
        }
        return genreRepository.findAll().stream().map(GenreDto::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public GenreDto get(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        return GenreDto.mapEntityToDto(genreEntity);
    }

    public QueryResponseWrapper<GenreDto> getGenres(SearchCriteria criteria) {
        Page<GenreDto> content = genreRepository.findAllWithPagination(criteria.composePageRequest());
        return new QueryResponseWrapper<>(content.getTotalElements(), content.getContent());
    }

    @Override
    public GenreDto update(GenreDto genreDto, Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        GenreEntity mapped = GenreDto.mapDtoToEntity(genreDto);
        mapped.setId(genreEntity.getId());
        GenreEntity savedGenre = genreRepository.save(mapped);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    @Override
    public void delete(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        genreRepository.delete(genreEntity);
    }
}
