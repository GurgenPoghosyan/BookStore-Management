package com.internship.bookstore.controller;

import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.service.CSVReaderService;
import com.internship.bookstore.service.GenreService;
import com.internship.bookstore.service.criteria.GenreSearchCriteria;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Gurgen Poghosyan
 */
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final CSVReaderService csvReaderService;

    @Autowired
    public GenreController(GenreService genreService, CSVReaderService csvReaderService) {
        this.genreService = genreService;
        this.csvReaderService = csvReaderService;
    }

    @PostMapping()
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto genreDto) {
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

    @PostMapping("/read-csv")
    public List<GenreDto> readGenre(@RequestParam("genres") MultipartFile multipartFile) {
        return csvReaderService.csvGenreProcessor(multipartFile);
    }
}
