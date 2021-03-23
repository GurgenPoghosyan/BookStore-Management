package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.PublisherEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gurgen Poghosyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDto {
    private Long id;

    private String name;

    public static PublisherDto mapEntityToDto(PublisherEntity entity) {
        if (entity == null) {
            return null;
        }
        PublisherDto dto = new PublisherDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static PublisherEntity mapDtoToEntity(PublisherDto publisherDto) {
        if (publisherDto == null) {
            return null;
        }
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setName(publisherDto.getName());
        return publisherEntity;
    }
}
