package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.persistence.entity.CollectionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gurgen Poghosyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {

    private Long id;

    private String name;

    private List<BookDto> books;

    private Long userId;

    public static CollectionDto mapEntityToDto(CollectionEntity entity) {
        if (entity == null) {
            return null;
        }
        CollectionDto dto = new CollectionDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUserId(entity.getUser().getId());
        List<BookEntity> books = entity.getBooks();
        if (!CollectionUtils.isEmpty(books)) {
            dto.setBooks(books.stream().map(BookDto::mapEntityToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public static CollectionEntity mapDtoToEntity(CollectionDto dto) {
        if (dto == null) {
            return null;
        }
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setName(dto.getName());
        return collectionEntity;
    }

}
