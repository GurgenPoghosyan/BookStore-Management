package com.internship.bookstore.controller;

import com.internship.bookstore.persistence.entity.UserDetailsEntity;
import com.internship.bookstore.service.UserDetailsService;
import com.internship.bookstore.service.criteria.UserDetailsSearchCriteria;
import com.internship.bookstore.service.dto.UserDetailsDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/user-details")
public class UserDetailsController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserDetailsController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping()
    public ResponseEntity<UserDetailsDto> createUserDetails(@RequestBody UserDetailsDto userDetailsDto) {
        UserDetailsDto dto = userDetailsService.create(userDetailsDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDto> getUserDetail(@PathVariable Long id) {
        UserDetailsDto dto = userDetailsService.getUserDetail(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<UserDetailsDto> getUserDetails(UserDetailsSearchCriteria criteria) {
        return userDetailsService.getUserDetails(criteria);
    }

    @PutMapping("/{id}")
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
