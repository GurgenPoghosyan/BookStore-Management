package com.internship.bookstore.controller;

import com.internship.bookstore.service.CollectionService;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.transform.request.collection.AddBookToCollectionRequest;
import com.internship.bookstore.transform.request.collection.CollectionCreateRequest;
import com.internship.bookstore.transform.request.collection.CollectionUpdateRequset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        CollectionDto dto = collectionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> getCollection(@PathVariable Long id) {
        CollectionDto dto = collectionService.get(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public List<CollectionDto> getCollections(@RequestParam(value = "name", required = false) String name) {
        return collectionService.getCollectionData(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionDto> updateCollection(@PathVariable Long id,
                                           @RequestBody CollectionUpdateRequset updateRequest) {
        CollectionDto dto = collectionService.update(updateRequest, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCollection(@PathVariable Long id) {
        collectionService.delete(id);
    }

    @PutMapping()
    public ResponseEntity<CollectionDto> addBookToCollections(@RequestBody AddBookToCollectionRequest request){
        CollectionDto dto = collectionService.addBookToCollection(request.getBookId(), request.getCollectionId());
        return ResponseEntity.ok(dto);
    }

}
