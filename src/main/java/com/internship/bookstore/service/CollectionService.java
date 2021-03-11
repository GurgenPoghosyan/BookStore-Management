package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.BookNotFoundException;
import com.internship.bookstore.common.exceptions.CollectionNotFoundException;
import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.BookRepository;
import com.internship.bookstore.persistence.repository.CollectionRepository;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.transform.request.collection.CollectionCreateRequest;
import com.internship.bookstore.transform.request.collection.CollectionUpdateRequset;
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
public class CollectionService implements CRUDService<CollectionCreateRequest,
        CollectionUpdateRequset, ResponseEntity<CollectionDto>, Long> {

    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository,
                             BookRepository bookRepository,
                             GenreRepository genreRepository) {
        this.collectionRepository = collectionRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public ResponseEntity<CollectionDto> create(CollectionCreateRequest request) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setName(request.getName());
        GenreEntity genreEntity = genreRepository.findByGenreName(request.getGenreName()).orElseThrow(() -> new GenreNotFoundException(request.getGenreName()));
        collectionEntity.setGenreEntity(genreEntity);
        CollectionEntity savedCollectionEntity = collectionRepository.save(collectionEntity);
        CollectionDto collectionDto = new CollectionDto();
        BeanUtils.copyProperties(savedCollectionEntity,collectionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(collectionDto);
    }

    @Override
    public ResponseEntity<CollectionDto> get(Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        CollectionDto collectionDto = new CollectionDto();
        BeanUtils.copyProperties(collectionEntity,collectionDto);
        return ResponseEntity.ok(collectionDto);
    }

    public List<CollectionEntity> getCollectionData(String name) {
        if (name != null) {
            return collectionRepository.findAll().stream().filter(book -> book.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return collectionRepository.findAll();
    }

    @Override
    public ResponseEntity<CollectionDto> update(CollectionUpdateRequset request, Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        collectionEntity.setName(request.getName());
        CollectionEntity updatedCollectionEntity = collectionRepository.save(collectionEntity);
        CollectionDto collectionDto = new CollectionDto();
        BeanUtils.copyProperties(updatedCollectionEntity,collectionDto);
        return ResponseEntity.ok(collectionDto);
    }

    @Override
    public void delete(Long id) {
        CollectionEntity collectionEntity = collectionRepository.findById(id).orElseThrow(() -> new CollectionNotFoundException(id));
        collectionRepository.delete(collectionEntity);
    }

    public ResponseEntity<CollectionDto> addBookToCollection(Long bookId, Long collectionId) {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException(bookId));
        CollectionEntity collectionEntity = collectionRepository.findById(collectionId).orElseThrow(() -> new CollectionNotFoundException(collectionId));
        collectionEntity.getBookEntities().add(bookEntity);
        CollectionEntity savedCollectionEntity = collectionRepository.save(collectionEntity);
        CollectionDto collectionDto = new CollectionDto();
        BeanUtils.copyProperties(savedCollectionEntity,collectionDto);
        return ResponseEntity.ok(collectionDto);
    }
}
