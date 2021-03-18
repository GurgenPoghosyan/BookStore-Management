package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.model.AuthorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Gurgen Poghosyan
 */
@Service
public class AuthorService implements CRUDService<AuthorDto, AuthorDto, AuthorDto, Long> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
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

    public List<AuthorWrapper> getAuthorData(String name) {
        if (name != null) {
            return authorRepository.findAllAuthors().stream().filter(author -> author.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return authorRepository.findAllAuthors();
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

    public static AuthorDto mapEntityToDto(AuthorEntity entity) {
        if (entity == null) {
            return null;
        }
        AuthorDto dto = new AuthorDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static AuthorEntity mapDtoToEntity(AuthorDto authorDto) {
        if (authorDto == null) {
            return null;
        }
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(authorDto.getName());
        return authorEntity;
    }

    public List<AuthorEntity> mapLongListToEntityList(List<Long> authors){
        List<AuthorEntity> list = new ArrayList<>();
        for (Long longId : authors) {
            AuthorEntity byId = authorRepository.findById(longId).orElseThrow(()->new AuthorNotFoundException(longId));
            list.add(byId);
        }
        return list;
    }
}
