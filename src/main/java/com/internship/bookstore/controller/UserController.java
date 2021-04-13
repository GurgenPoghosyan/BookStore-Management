package com.internship.bookstore.controller;

import com.internship.bookstore.security.session.SessionUser;
import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.UserService;
import com.internship.bookstore.service.criteria.UserSearchCriteria;
import com.internship.bookstore.service.dto.FileStorageDto;
import com.internship.bookstore.service.dto.UserDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.internship.bookstore.security.session.SessionUser.SESSION_USER_KEY;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@SessionAttributes(SESSION_USER_KEY)
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto dto = userService.getUser(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public QueryResponseWrapper<UserDto> getUsers(UserSearchCriteria criteria) {
        return userService.getUsers(criteria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserDto userDto) {
        UserDto dto = userService.update(userDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

//    @PostMapping("/upload")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public void readUser(@RequestParam("users") MultipartFile multipartFile) {
//        csvReaderService.csvUsersProcessor(multipartFile);
//    }

    @PostMapping("/files/upload")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<FileStorageDto> uploadFile(@RequestParam("image") MultipartFile file,
                                                     @ModelAttribute(SESSION_USER_KEY) SessionUser sessionUser) {
        FileStorageDto fileStorageDto = userService.uploadFile(file,sessionUser);
        return ResponseEntity.ok(fileStorageDto);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<String, String> readUser(@RequestParam("users") MultipartFile multipartFile) {
        return userService.saveUsers(multipartFile);
    }

    @GetMapping("/session")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SessionUser> getSessionUser(@ModelAttribute(SESSION_USER_KEY) SessionUser sessionUser) {
        return ResponseEntity.ok(sessionUser);
    }
}
