package com.internship.bookstore.service.dto;

import com.internship.bookstore.persistence.entity.CommunityEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gurgen Poghosyan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {

    private Long id;

    private String name;

    private String zip;

    public static CommunityDto mapEntityToDto(CommunityEntity entity) {
        if (entity == null) {
            return null;
        }
        CommunityDto dto = new CommunityDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setZip(entity.getZip());
        return dto;
    }

    public static CommunityEntity mapDtoToEntity(CommunityDto communityDto) {
        if (communityDto == null) {
            return null;
        }
        CommunityEntity communityEntity = new CommunityEntity();
        communityEntity.setName(communityDto.getName());
        communityEntity.setZip(communityDto.getZip());
        return communityEntity;
    }
}
