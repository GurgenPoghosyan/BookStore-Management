package com.internship.bookstore.controller;

import com.internship.bookstore.service.RoleService;
import com.internship.bookstore.service.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping()
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        if (roleDto.getName() == null) {
            throw new NullPointerException("Role name is required");
        }
        RoleDto dto = roleService.create(roleDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long id) {
        RoleDto dto = roleService.get(id);
        return ResponseEntity.ok(dto);
    }

//    @GetMapping()
//    public QueryResponseWrapper<RoleDto> getRoles() {
//        return authorService.getAuthors();
//    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateAuthor(@PathVariable Long id,
                                                  @RequestBody RoleDto roleDto) {
        RoleDto dto = roleService.update(roleDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        roleService.delete(id);
    }
}
