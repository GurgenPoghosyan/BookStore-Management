package com.internship.bookstore.controller.collection;

import com.internship.bookstore.entity.book.Book;
import com.internship.bookstore.entity.collection.Collection;
import com.internship.bookstore.service.collection.CollectionService;
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
    public ResponseEntity<Collection> createCollection(@RequestBody CollectionCreateRequest request) {
        return collectionService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollection(@PathVariable Long id) {
        return collectionService.get(id);
    }

    @GetMapping
    public List<Collection> getCollections(@RequestParam(value = "name", required = false) String name) {
        return collectionService.getCollectionData(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collection> updateCollection(@PathVariable Long id,
                                           @RequestBody CollectionUpdateRequset updateRequest) {
        return collectionService.update(updateRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCollection(@PathVariable Long id) {
        collectionService.delete(id);
    }

    @PutMapping()
    public ResponseEntity<Collection> addBookToCollections(@RequestBody AddBookToCollectionRequest request){
        return  collectionService.addBookToCollection(request.getBookId(), request.getCollectionId());
    }

}
