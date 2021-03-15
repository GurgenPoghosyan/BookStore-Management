package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.CollectionEntity;
import com.internship.bookstore.persistence.entity.GenreEntity;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class CollectionDto {

    private Long id;

    private String name;

    private List<String> books;

    public static CollectionDto mapEntityToDto(CollectionEntity entity){
        if (entity==null){
            return null;
        }
        CollectionDto dto = new CollectionDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        List<BookEntity> books = entity.getBooks();
        if (!CollectionUtils.isEmpty(books)){
            dto.setBooks(books.stream().map(BookEntity::getName).collect(Collectors.toList()));
        }
    return dto;
    }

}
