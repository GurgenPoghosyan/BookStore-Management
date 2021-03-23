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
public class AuthorService implements CRUDService<AuthorDto> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public AuthorDto create(AuthorDto authorDto) {
        AuthorEntity authorEntity = AuthorDto.mapDtoToEntity(authorDto);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return AuthorDto.mapEntityToDto(savedAuthorEntity);
    }

    @Override
    public AuthorDto get(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        return AuthorDto.mapEntityToDto(authorEntity);
    }

    public List<AuthorDto> getAuthors(String name) {
        if (name != null) {
            return authorRepository.findAll().stream().filter(author -> author.getName().equalsIgnoreCase(name)).collect(Collectors.toList())
                    .stream().map(AuthorDto::mapEntityToDto).collect(Collectors.toList());
        }
        return authorRepository.findAll().stream().map(AuthorDto::mapEntityToDto).collect(Collectors.toList());
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
        return AuthorDto.mapEntityToDto(updatedAuthorEntity);
    }

    @Override
    public void delete(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(authorEntity);
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
