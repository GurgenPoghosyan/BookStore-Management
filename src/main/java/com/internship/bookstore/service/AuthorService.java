package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.model.AuthorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Gurgen Poghosyan
 */
@Service
public class AuthorService implements CRUDService<AuthorDto, AuthorDto,AuthorDto,Long> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDto create(AuthorDto authorDto) {
        AuthorEntity authorEntity = AuthorEntity.mapDtoToEntity(authorDto);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        AuthorDto.mapEntityToDto(savedAuthorEntity);
        return authorDto;
    }

    @Override
    public AuthorDto get(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        return AuthorDto.mapEntityToDto(authorEntity);
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
        return AuthorDto.mapEntityToDto(updatedAuthorEntity);
    }

    @Override
    public void delete(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(authorEntity);
    }

//    public List<String> getAuthorBooks(Long id) {
//        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
//        AuthorDto dto = AuthorDto.mapEntityToDto(authorEntity);
//        return dto.getBooks();
//    }
}
