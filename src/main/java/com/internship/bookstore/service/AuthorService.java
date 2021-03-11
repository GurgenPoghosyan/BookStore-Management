package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.transform.request.author.AuthorCreateRequest;
import com.internship.bookstore.transform.request.author.AuthorUpdateRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Gurgen Poghosyan
 */
@Service
public class AuthorService implements CRUDService<AuthorCreateRequest, AuthorUpdateRequest,ResponseEntity<AuthorDto>,Long> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public ResponseEntity<AuthorDto> create(AuthorCreateRequest createRequest) {
        AuthorEntity authorEntity = new AuthorEntity();
        BeanUtils.copyProperties(createRequest, authorEntity);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        AuthorDto authorDto = new AuthorDto();
        BeanUtils.copyProperties(savedAuthorEntity,authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorDto);
    }

    @Override
    public ResponseEntity<AuthorDto> get(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        AuthorDto authorDto = new AuthorDto();
        BeanUtils.copyProperties(authorEntity,authorDto);
        return ResponseEntity.ok(authorDto);
    }

    public List<AuthorEntity> getAuthorData(String name) {
        if (name != null) {
            return authorRepository.findAll().stream().filter(author -> author.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return authorRepository.findAll();
    }

    @Override
    public ResponseEntity<AuthorDto> update(AuthorUpdateRequest updateRequest, Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        BeanUtils.copyProperties(updateRequest, authorEntity);
        AuthorEntity updatedAuthorEntity = authorRepository.save(authorEntity);
        AuthorDto authorDto = new AuthorDto();
        BeanUtils.copyProperties(updatedAuthorEntity,authorDto);
        return ResponseEntity.ok(authorDto);
    }

    @Override
    public void delete(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(authorEntity);
    }

    public List<BookEntity> getAuthorBooks(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        return authorEntity.getBookEntities();
    }
}
