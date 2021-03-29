package com.internship.bookstore.controller;

import com.internship.bookstore.service.CollectionService;
import com.internship.bookstore.service.criteria.CollectionSearchCriteria;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import com.internship.bookstore.transform.requestbody.collection.AddBookToCollectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService collectionService;

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping
    public ResponseEntity<CollectionDto> createCollection(@RequestBody CollectionDto collectionDto) {
        CollectionDto dto = collectionService.create(collectionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> getCollection(@PathVariable Long id) {
        CollectionDto dto = collectionService.getCollection(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<CollectionDto> getCollections(@RequestBody CollectionSearchCriteria criteria) {
        return collectionService.getCollections(criteria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionDto> updateCollection(@PathVariable Long id,
                                                          @RequestBody CollectionDto collectionDto) {
        CollectionDto dto = collectionService.update(collectionDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCollection(@PathVariable Long id) {
        collectionService.delete(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<CollectionDto> addBookToCollections(@PathVariable Long id, @RequestBody AddBookToCollectionRequest request) {
        CollectionDto dto = collectionService.addBookToCollection(id, request.getCollectionId());
        return ResponseEntity.ok(dto);
    }

}
