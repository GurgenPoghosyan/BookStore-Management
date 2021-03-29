package com.internship.bookstore.controller;

import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.UserService;
import com.internship.bookstore.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CSVReaderService csvReaderService;

    @Autowired
    public UserController(UserService userService, CSVReaderService csvReaderService) {
        this.userService = userService;
        this.csvReaderService = csvReaderService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto dto = userService.create(userDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto dto = userService.getUser(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserDto userDto) {
        UserDto dto = userService.update(userDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        userService.delete(id);
    }


    @PostMapping("/read-csv")
    public void readUser(@RequestParam("users") MultipartFile multipartFile) {
        csvReaderService.csvUsersProcessor(multipartFile);
    }
}
