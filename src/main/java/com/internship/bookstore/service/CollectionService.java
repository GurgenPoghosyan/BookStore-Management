package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.CollectionNotFoundException;
import com.internship.bookstore.common.exceptions.UserNotFoundException;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.persistence.entity.UserEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.CollectionRepository;
import com.internship.bookstore.persistence.repository.UserRepository;
import com.internship.bookstore.security.session.SessionUser;
import com.internship.bookstore.service.criteria.CollectionSearchCriteria;
import com.internship.bookstore.service.dto.BookDto;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CollectionDto create(CollectionDto collectionDto, SessionUser sessionUser) {
        if (collectionDto.getName() == null) {
            throw new NullPointerException("Collection name is required");
        }
        CollectionEntity collectionEntity = CollectionDto.mapDtoToEntity(collectionDto);
        List<BookDto> listOfBooks = collectionDto.getBooks();
        if (listOfBooks == null) {
            throw new NullPointerException("Book list is required");
        }
        for (BookDto book : listOfBooks) {
            BookEntity bookEntity = bookRepository.findByName(book.getName());
            collectionEntity.getBooks().add(bookEntity);
        }
        CollectionEntity savedCollectionEntity = collectionRepository.save(collectionEntity);
        UserEntity userEntity = userRepository.findById(sessionUser.getId()).orElseThrow(() -> new UserNotFoundException(sessionUser.getId()));
        userEntity.getBookCollections().add(savedCollectionEntity);
        UserEntity savedUser = userRepository.save(userEntity);
        collectionEntity.setUser(savedUser);
        collectionRepository.save(collectionEntity);
        return CollectionDto.mapEntityToDto(savedCollectionEntity);
    }

    public CollectionDto getCollection(Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        return CollectionDto.mapEntityToDto(collectionEntity);
    }

    public QueryResponseWrapper<CollectionDto> getCollections(CollectionSearchCriteria criteria) {
        Page<CollectionEntity> entityContent = collectionRepository.find(criteria.getName(), criteria.composePageRequest());
        Page<CollectionDto> dtoContent = entityContent.map(CollectionDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public CollectionDto update(CollectionDto collectionDto, Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        List<BookDto> listOfBooks = collectionDto.getBooks();
        if (listOfBooks != null) {
            List<BookEntity> listOfBookEntities = new ArrayList<>();
            for (BookDto book : listOfBooks) {
                BookEntity bookEntity = bookRepository.findByName(book.getName());
                listOfBookEntities.add(bookEntity);
            }
            collectionEntity.setBooks(listOfBookEntities);
        }
        if (collectionDto.getName() != null) {
            collectionEntity.setName(collectionDto.getName());
        }
        CollectionEntity updatedCollectionEntity = collectionRepository.save(collectionEntity);
        return CollectionDto.mapEntityToDto(updatedCollectionEntity);
    }

    public void delete(Long id) {
        collectionRepository.deleteById(id);
    }

    public CollectionDto addBookToCollection(Long bookId, Long collectionId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        CollectionEntity collectionEntity = collectionRepository.findById(collectionId).orElseThrow(() -> new CollectionNotFoundException(collectionId));
        collectionEntity.getBooks().add(bookEntity);
        CollectionEntity savedCollectionEntity = collectionRepository.save(collectionEntity);
        return CollectionDto.mapEntityToDto(savedCollectionEntity);
    }


}
