package com.internship.bookstore.controller;

import com.internship.bookstore.service.UserDetailsService;
import com.internship.bookstore.service.criteria.UserDetailsSearchCriteria;
import com.internship.bookstore.service.dto.UserDetailsDto;
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
@RequestMapping("/user-details")

public class UserDetailsController {

    private final UserDetailsService userDetailsService;

    @PostMapping()
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserDetailsDto> createUserDetails(@RequestBody UserDetailsDto userDetailsDto) {
        UserDetailsDto dto = userDetailsService.create(userDetailsDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDetailsDto> getUserDetail(@PathVariable Long id) {
        UserDetailsDto dto = userDetailsService.getUserDetail(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public QueryResponseWrapper<UserDetailsDto> getUserDetails(UserDetailsSearchCriteria criteria) {
        return userDetailsService.getUserDetails(criteria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserDetailsDto> updateUserDetails(@PathVariable Long id,
                                                            @RequestBody UserDetailsDto userDetailsDto) {
        UserDetailsDto dto = userDetailsService.update(userDetailsDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserDetails(@PathVariable Long id) {
        userDetailsService.delete(id);
    }
}
