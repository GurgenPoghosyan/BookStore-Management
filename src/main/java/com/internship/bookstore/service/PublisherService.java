package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.PublisherNotFoundException;
import com.internship.bookstore.persistence.entity.PublisherEntity;
import com.internship.bookstore.persistence.repository.PublisherRepository;
import com.internship.bookstore.service.criteria.PublisherSearchCriteria;
import com.internship.bookstore.service.dto.PublisherDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherDto create(PublisherDto publisherDto) {
        PublisherEntity publisherEntity = PublisherDto.mapDtoToEntity(publisherDto);
        PublisherEntity savedPublisherEntity = publisherRepository.save(publisherEntity);
        return PublisherDto.mapEntityToDto(savedPublisherEntity);
    }

    public PublisherDto getPublisher(Long id) {
        PublisherEntity publisherEntity = publisherRepository.findById(id).orElseThrow(() -> new PublisherNotFoundException(id));
        return PublisherDto.mapEntityToDto(publisherEntity);
    }

    public QueryResponseWrapper<PublisherDto> getPublishers(PublisherSearchCriteria criteria) {
        Page<PublisherEntity> entityContent = publisherRepository.find(criteria.getName(), criteria.composePageRequest());
        Page<PublisherDto> dtoContent = entityContent.map(PublisherDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public PublisherDto update(PublisherDto publisherDto, Long id) {
        PublisherEntity publisherEntity = publisherRepository.findById(id).orElseThrow(() -> new PublisherNotFoundException(id));
        publisherEntity.setName(publisherDto.getName());
        PublisherEntity updatedPublisherEntity = publisherRepository.save(publisherEntity);
        return PublisherDto.mapEntityToDto(updatedPublisherEntity);
    }

    public void delete(Long id) {
        publisherRepository.deleteById(id);
    }

}
