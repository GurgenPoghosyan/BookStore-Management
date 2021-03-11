package com.internship.bookstore.controller;

import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.service.CollectionService;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.transform.request.collection.AddBookToCollectionRequest;
import com.internship.bookstore.transform.request.collection.CollectionCreateRequest;
import com.internship.bookstore.transform.request.collection.CollectionUpdateRequset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/api/v1/collections")
public class CollectionController {

    private final CollectionService collectionService;

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }
    @PostMapping
    public ResponseEntity<CollectionDto> createCollection(@RequestBody CollectionCreateRequest request) {
        return collectionService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> getCollection(@PathVariable Long id) {
        return collectionService.get(id);
    }

    @GetMapping
    public List<CollectionEntity> getCollections(@RequestParam(value = "name", required = false) String name) {
        return collectionService.getCollectionData(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionDto> updateCollection(@PathVariable Long id,
                                           @RequestBody CollectionUpdateRequset updateRequest) {
        return collectionService.update(updateRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCollection(@PathVariable Long id) {
        collectionService.delete(id);
    }

    @PutMapping()
    public ResponseEntity<CollectionDto> addBookToCollections(@RequestBody AddBookToCollectionRequest request){
        return  collectionService.addBookToCollection(request.getBookId(), request.getCollectionId());
    }

}
