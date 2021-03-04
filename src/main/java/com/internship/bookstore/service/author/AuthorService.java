package com.internship.bookstore.service.author;

import com.internship.bookstore.common.exceptions.AuthorNotFoundException;
import com.internship.bookstore.entity.author.Author;
import com.internship.bookstore.persistence.author.AuthorRepository;
import com.internship.bookstore.transform.request.author.AuthorCreateRequest;
import com.internship.bookstore.transform.request.author.AuthorUpdateRequest;
import com.internship.bookstore.transform.response.author.AuthorResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Gurgen Poghosyan
 */
@Service
public class AuthorService implements CreateSupported<AuthorCreateRequest, AuthorResponse>,
        GetSupported<Long, AuthorResponse>,
        UpdateSupported<AuthorResponse, AuthorUpdateRequest, Long>,
        DeleteSupported<Long> {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorResponse create(AuthorCreateRequest createRequest) {
        Author author = new Author();
        BeanUtils.copyProperties(createRequest, author);
        Author savedAuthor = authorRepository.save(author);
        AuthorResponse response = new AuthorResponse();
        BeanUtils.copyProperties(savedAuthor, response);
        return response;
    }

    @Override
    public AuthorResponse get(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        AuthorResponse response = new AuthorResponse();
        BeanUtils.copyProperties(author, response);
        return response;
    }

    public List<Author> getAuthorData(String name) {
        if (name != null) {
            return authorRepository.findAll().stream().filter(author -> author.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return authorRepository.findAll();
    }

    @Override
    public AuthorResponse update(AuthorUpdateRequest updateRequest, Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        BeanUtils.copyProperties(updateRequest, author);
        Author updatedAuthor = authorRepository.save(author);
        AuthorResponse response = new AuthorResponse();
        BeanUtils.copyProperties(updatedAuthor, response);
        return response;
    }

    @Override
    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        authorRepository.delete(author);
    }
}
