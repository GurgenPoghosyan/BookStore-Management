package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.persistence.entity.AuthorEntity;
import com.internship.bookstore.persistence.repository.AuthorRepository;
import com.internship.bookstore.service.criteria.AuthorSearchCriteria;
import com.internship.bookstore.service.dto.AuthorDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDto create(AuthorDto authorDto) {
        if (authorDto.getName() == null) {
            throw new NullPointerException("Author name is required");
        }
        AuthorEntity authorEntity = AuthorDto.mapDtoToEntity(authorDto);
        AuthorEntity savedAuthorEntity = authorRepository.save(authorEntity);
        return AuthorDto.mapEntityToDto(savedAuthorEntity);
    }

    public AuthorDto getAuthor(Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        return AuthorDto.mapEntityToDto(authorEntity);
    }

    public QueryResponseWrapper<AuthorDto> getAuthors(AuthorSearchCriteria criteria) {
        Page<AuthorEntity> entityContent = authorRepository.find(criteria.getName(), criteria.composePageRequest());
        Page<AuthorDto> dtoContent = entityContent.map(AuthorDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public AuthorDto update(AuthorDto authorDto, Long id) {
        AuthorEntity authorEntity = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        if (authorDto.getName() != null) {
            authorEntity.setName(authorDto.getName());
        }
        AuthorEntity updatedAuthorEntity = authorRepository.save(authorEntity);
        return AuthorDto.mapEntityToDto(updatedAuthorEntity);
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
