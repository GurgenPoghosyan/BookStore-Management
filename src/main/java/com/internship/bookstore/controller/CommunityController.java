package com.internship.bookstore.controller;

import com.internship.bookstore.service.CommunityService;
import com.internship.bookstore.service.criteria.CommunitySearchCriteria;
import com.internship.bookstore.service.dto.CommunityDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping()
    public ResponseEntity<CommunityDto> createCommunity(@RequestBody CommunityDto communityDto) {
        CommunityDto dto = communityService.create(communityDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable Long id) {
        CommunityDto dto = communityService.getCommunity(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<CommunityDto> getCommunities(CommunitySearchCriteria criteria) {
        return communityService.getCommunities(criteria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityDto> updateCommunity(@PathVariable Long id,
                                                        @RequestBody CommunityDto communityDto) {
        CommunityDto dto = communityService.update(communityDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCommunity(@PathVariable Long id) {
        communityService.delete(id);
    }
}
