package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.service.criteria.SearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
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
public class AuthorService implements CRUDService<AuthorDto, Long> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public static AuthorDto mapEntityToDto(AuthorEntity entity) {
        if (entity == null) {
            return null;
        }
        AuthorDto dto = new AuthorDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    @Override
    public AuthorDto create(AuthorDto authorDto) {
        AuthorEntity authorEntity = mapDtoToEntity(authorDto);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        mapEntityToDto(savedAuthorEntity);
        return authorDto;
    }

    @Override
    public AuthorDto get(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        return mapEntityToDto(authorEntity);
    }

    public List<AuthorDto> getAuthors(String name) {
        if (name != null) {
            return authorRepository.findAll().stream().filter(author -> author.getName().equalsIgnoreCase(name)).collect(Collectors.toList())
                    .stream().map(AuthorService::mapEntityToDto).collect(Collectors.toList());
        }
        return authorRepository.findAll().stream().map(AuthorService::mapEntityToDto).collect(Collectors.toList());
    }

    public QueryResponseWrapper<AuthorDto> getAuthors(SearchCriteria criteria) {
        Page<AuthorDto> content = authorRepository.findAllWithPagination(criteria.composePageRequest());
        return new QueryResponseWrapper<>(content.getTotalElements(), content.getContent());
    }

    @Override
    public AuthorDto update(AuthorDto authorDto, Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorEntity.setName(authorDto.getName());
        AuthorEntity updatedAuthorEntity = authorRepository.save(authorEntity);
        return mapEntityToDto(updatedAuthorEntity);
    }

    @Override
    public void delete(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(authorEntity);
    }

    public AuthorEntity mapDtoToEntity(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(authorDto.getName());
        return authorEntity;
    }

    public List<AuthorEntity> mapLongListToEntityList(List<Long> listOfAuthors) {
        List<AuthorEntity> list = new ArrayList<>();
        for (Long author : listOfAuthors) {
            AuthorEntity authorEntity = authorRepository.findById(author).orElseThrow(() -> new AuthorNotFoundException(author));
            list.add(authorEntity);
        }
        return list;
    }
}
