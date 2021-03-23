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

    private List<Long> books;

    public CollectionDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CollectionDto mapEntityToDto(CollectionEntity entity) {
        if (entity == null) {
            return null;
        }
        CollectionDto dto = new CollectionDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        List<BookEntity> books = entity.getBooks();
        if (!CollectionUtils.isEmpty(books)) {
            dto.setBooks(entity.getBooks().stream().map(BookEntity::getId).collect(Collectors.toList()));
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
