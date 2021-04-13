package com.internship.bookstore.controller;

import com.internship.bookstore.service.PublisherService;
import com.internship.bookstore.service.criteria.PublisherSearchCriteria;
import com.internship.bookstore.service.dto.PublisherDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/publishers")
@PreAuthorize("hasAuthority('EDITOR')")
public class PublisherController {
    private final PublisherService publisherService;

    @PostMapping()
    public ResponseEntity<PublisherDto> createPublisher(@RequestBody PublisherDto publisherDto) {
        if (publisherDto.getName() == null) {
            throw new NullPointerException("Publisher name is required");
        }
        PublisherDto dto = publisherService.create(publisherDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDto> getPublisher(@PathVariable Long id) {
        PublisherDto dto = publisherService.getPublisher(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<PublisherDto> getPublishers(PublisherSearchCriteria criteria) {
        return publisherService.getPublishers(criteria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDto> updatePublisher(@PathVariable Long id,
                                                  @RequestBody PublisherDto publisherDto) {
        PublisherDto dto = publisherService.update(publisherDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
    }
}
