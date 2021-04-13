package com.internship.bookstore.controller;

import com.internship.bookstore.security.session.SessionUser;
import com.internship.bookstore.service.CollectionService;
import com.internship.bookstore.service.criteria.CollectionSearchCriteria;
import com.internship.bookstore.service.dto.CollectionDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import com.internship.bookstore.transform.requestbody.collection.AddBookToCollectionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.internship.bookstore.security.session.SessionUser.SESSION_USER_KEY;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@SessionAttributes(SESSION_USER_KEY)
@RequestMapping("/collections")
@PreAuthorize("hasAuthority('USER')")
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<CollectionDto> createCollection(@RequestBody CollectionDto collectionDto,
                                                          @ModelAttribute(SESSION_USER_KEY) SessionUser sessionUser) {
        if (collectionDto.getName() == null) {
            throw new NullPointerException("Collection name is required");
        }
        if (collectionDto.getBooks() == null) {
            throw new NullPointerException("Book list is required");
        }
        CollectionDto dto = collectionService.create(collectionDto,sessionUser);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> getCollection(@PathVariable Long id) {
        CollectionDto dto = collectionService.getCollection(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<CollectionDto> getCollections(CollectionSearchCriteria criteria) {
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
