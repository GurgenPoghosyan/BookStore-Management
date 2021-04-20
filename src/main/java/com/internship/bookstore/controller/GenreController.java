package com.internship.bookstore.controller;

import com.internship.bookstore.common.util.CsvParser;
import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.GenreService;
import com.internship.bookstore.service.criteria.GenreSearchCriteria;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
@PreAuthorize("hasAuthority('ADMIN')")
public class GenreController {

    private final GenreService genreService;
    private final CSVReaderService csvReaderService;
    private final CsvParser genreCsvParser;

    @PostMapping()
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto genreDto) {
        if (genreDto.getName() == null) {
            throw new NullPointerException("Genre name is required");
        }
        GenreDto dto = genreService.create(genreDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenre(@PathVariable Long id) {
        GenreDto dto = genreService.getGenre(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public QueryResponseWrapper<GenreDto> getGenres(GenreSearchCriteria criteria) {
        return genreService.getGenres(criteria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable Long id,
                                                @RequestBody GenreDto genreDto) {
        GenreDto dto = genreService.update(genreDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.delete(id);
    }

    @PostMapping("/upload")
    public void readGenre(@RequestParam("genres") MultipartFile multipartFile) {
        genreService.saveGenres(multipartFile);
    }
}
