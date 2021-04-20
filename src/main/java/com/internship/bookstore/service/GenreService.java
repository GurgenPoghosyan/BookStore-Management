package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.GenreNotFoundException;
import com.internship.bookstore.common.threads.GenreSaveThread;
import com.internship.bookstore.common.util.CsvParser;
import com.internship.bookstore.common.util.FileSplitter;
import com.internship.bookstore.persistence.entity.GenreEntity;
import com.internship.bookstore.persistence.repository.GenreRepository;
import com.internship.bookstore.service.criteria.GenreSearchCriteria;
import com.internship.bookstore.service.dto.GenreDto;
import com.internship.bookstore.service.model.wrapper.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final CsvParser genreCsvParser;
    @Value("${genre.file.split-dir}")
    private String uploadDir;

    public GenreDto create(GenreDto genreDto) {
        GenreEntity genreEntity = GenreDto.mapDtoToEntity(genreDto);
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    public GenreDto getGenre(Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        return GenreDto.mapEntityToDto(genreEntity);
    }

    public QueryResponseWrapper<GenreDto> getGenres(GenreSearchCriteria criteria) {
        Page<GenreEntity> contentEntity = genreRepository.find(criteria.getName(), criteria.composePageRequest());
        Page<GenreDto> contentDto = contentEntity.map(GenreDto::mapEntityToDto);
        return new QueryResponseWrapper<>(contentDto.getTotalElements(), contentDto.getContent());
    }

    public GenreDto update(GenreDto genreDto, Long id) {
        GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(() -> new GenreNotFoundException(id));
        if (genreDto.getName() != null) {
            genreEntity.setGenreName(genreDto.getName());
        }
        GenreEntity savedGenre = genreRepository.save(genreEntity);
        return GenreDto.mapEntityToDto(savedGenre);
    }

    public void delete(Long id) {
        genreRepository.deleteById(id);
    }


    public void saveGenres(MultipartFile csvFile) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        String pathname = String.join(File.separator, uploadDir, csvFile.getOriginalFilename());
        File parentCsv = new File(pathname);
        FileSplitter fileSplitter = new FileSplitter();
        parentCsv.mkdirs();
        try {
            csvFile.transferTo(parentCsv);
            List<File> files = fileSplitter.splitFile(parentCsv, 50);
            for (File file : files) {
                GenreSaveThread thread = new GenreSaveThread(this, genreCsvParser, file);
                executorService.submit(thread);
            }
            executorService.shutdown();
//            FileUtils.deleteDirectory(parentCsv.getParentFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll(List<GenreEntity> genres) {
        for (GenreEntity genreEntity : genres) {
            GenreEntity entity = genreRepository.findByGenreName(genreEntity.getGenreName());
            if (entity == null) {
                genreRepository.save(genreEntity);
            }
        }
    }
}
