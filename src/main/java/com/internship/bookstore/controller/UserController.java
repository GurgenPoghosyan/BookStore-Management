package com.internship.bookstore.controller;

import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.UserService;
import com.internship.bookstore.service.criteria.UserSearchCriteria;
import com.internship.bookstore.service.dto.UserDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CSVReaderService csvReaderService;

    @PostMapping("/registration")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto dto = userService.create(userDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto dto = userService.getUser(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public QueryResponseWrapper<UserDto> getUsers(UserSearchCriteria criteria) {
        return userService.getUsers(criteria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserDto userDto) {
        UserDto dto = userService.update(userDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public void readUser(@RequestParam("users") MultipartFile multipartFile) {
        csvReaderService.csvUsersProcessor(multipartFile);
    }

//    @PostMapping("/upload")
//    public void readUser(@RequestParam("users") MultipartFile multipartFile) {
//        userService.saveUsers(multipartFile);
//    }
}
