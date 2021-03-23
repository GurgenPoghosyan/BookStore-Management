package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.CollectionNotFoundException;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.CollectionRepository;
import com.internship.bookstore.service.criteria.SearchCriteria;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class CollectionService implements CRUDService<CollectionDto> {

    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository,
                             BookRepository bookRepository,
                             BookService bookService) {
        this.collectionRepository = collectionRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }


    @Override
    public CollectionDto create(CollectionDto collectionDto) {
        CollectionEntity collectionEntity = CollectionDto.mapDtoToEntity(collectionDto);
        collectionEntity.setBooks(bookService.mapLongListToEntityList(collectionDto.getBooks()));
        CollectionEntity savedCollectionEntity = collectionRepository.save(collectionEntity);
        return CollectionDto.mapEntityToDto(savedCollectionEntity);
    }

    @Override
    public CollectionDto get(Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        return CollectionDto.mapEntityToDto(collectionEntity);
    }

    public List<CollectionDto> getCollections(String name) {
        if (name != null) {
            List<CollectionEntity> collect = collectionRepository.findAll().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
            return collect.stream().map(CollectionDto::mapEntityToDto).collect(Collectors.toList());
        }
        List<CollectionEntity> all = collectionRepository.findAll();
        return all.stream().map(CollectionDto::mapEntityToDto).collect(Collectors.toList());
    }

    public QueryResponseWrapper<CollectionDto> getCollections(SearchCriteria criteria) {
        Page<CollectionDto> content = collectionRepository.findAllWithPagination(criteria.composePageRequest());
        return new QueryResponseWrapper<>(content.getTotalElements(), content.getContent());
    }

    @Override
    public CollectionDto update(CollectionDto collectionDto, Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        CollectionEntity mapped = CollectionDto.mapDtoToEntity(collectionDto);
        collectionEntity.setBooks(bookService.mapLongListToEntityList(collectionDto.getBooks()));
        mapped.setId(collectionEntity.getId());
        CollectionEntity updatedCollectionEntity = collectionRepository.save(mapped);
        return CollectionDto.mapEntityToDto(updatedCollectionEntity);
    }

    @Override
    public void delete(Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        collectionRepository.delete(collectionEntity);
    }

    public CollectionDto addBookToCollection(Long bookId, Long collectionId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        CollectionEntity collectionEntity = collectionRepository.findById(collectionId).orElseThrow(() -> new CollectionNotFoundException(collectionId));
        collectionEntity.getBooks().add(bookEntity);
        CollectionEntity savedCollectionEntity = collectionRepository.save(collectionEntity);
        return CollectionDto.mapEntityToDto(savedCollectionEntity);
    }


}
