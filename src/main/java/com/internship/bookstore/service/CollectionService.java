package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.CollectionNotFoundException;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.CollectionRepository;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.transform.request.collection.CollectionCreateRequest;
import com.internship.bookstore.transform.request.collection.CollectionUpdateRequset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Service
public class CollectionService implements CRUDService<CollectionCreateRequest,
        CollectionUpdateRequset, CollectionDto, Long> {

    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository,
                             BookRepository bookRepository) {
        this.collectionRepository = collectionRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public CollectionDto create(CollectionCreateRequest request) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setName(request.getName());
        CollectionEntity savedCollectionEntity = collectionRepository.save(collectionEntity);
        return CollectionDto.mapEntityToDto(savedCollectionEntity);
    }

    @Override
    public CollectionDto get(Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        return CollectionDto.mapEntityToDto(collectionEntity);
    }

    public List<CollectionDto> getCollectionData(String name) {
        if (name != null) {
            List<CollectionEntity> collect = collectionRepository.findAll().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
            return collect.stream().map(CollectionDto::mapEntityToDto).collect(Collectors.toList());
        }
        List<CollectionEntity> all = collectionRepository.findAll();
        return all.stream().map(CollectionDto::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public CollectionDto update(CollectionUpdateRequset request, Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        collectionEntity.setName(request.getName());
        CollectionEntity updatedCollectionEntity = collectionRepository.save(collectionEntity);
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
