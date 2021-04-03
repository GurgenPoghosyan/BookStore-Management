package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.CommunityNotFoundException;
import com.internship.bookstore.persistence.entity.CommunityEntity;
import com.internship.bookstore.persistence.repository.CommunityRepository;
import com.internship.bookstore.service.criteria.CommunitySearchCriteria;
import com.internship.bookstore.service.dto.CommunityDto;
import com.internship.bookstore.service.model.QueryResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    public CommunityDto create(CommunityDto dto) {
        if (dto.getName() == null) {
            throw new NullPointerException("Community name is required");
        }
        CommunityEntity communityEntity = CommunityDto.mapDtoToEntity(dto);
        CommunityEntity savedCommunity = communityRepository.save(communityEntity);
        return CommunityDto.mapEntityToDto(savedCommunity);
    }

    public CommunityDto getCommunity(Long id) {
        CommunityEntity communityEntity = communityRepository.findById(id).orElseThrow(() -> new CommunityNotFoundException(id));
        return CommunityDto.mapEntityToDto(communityEntity);
    }

    public QueryResponseWrapper<CommunityDto> getCommunities(CommunitySearchCriteria criteria) {
        Page<CommunityEntity> entityContent = communityRepository.find(criteria.getName(), criteria.getZip(), criteria.composePageRequest());
        Page<CommunityDto> dtoContent = entityContent.map(CommunityDto::mapEntityToDto);
        return new QueryResponseWrapper<>(dtoContent.getTotalElements(), dtoContent.getContent());
    }

    public CommunityDto update(CommunityDto dto, Long id) {
        CommunityEntity communityEntity = communityRepository.findById(id).orElseThrow(() -> new CommunityNotFoundException(id));
        if (dto.getName() != null) {
            communityEntity.setName(dto.getName());
        }
        CommunityEntity updatedCommunity = communityRepository.save(communityEntity);
        return CommunityDto.mapEntityToDto(updatedCommunity);
    }

    public void delete(Long id) {
        communityRepository.deleteById(id);
    }
}
