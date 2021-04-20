package com.internship.bookstore.service;

import com.internship.bookstore.common.exceptions.RoleNotFoundException;
import com.internship.bookstore.persistence.entity.RoleEntity;
import com.internship.bookstore.persistence.repository.RoleRepository;
import com.internship.bookstore.service.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Gurgen Poghosyan
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleDto create(RoleDto roleDto) {
        RoleEntity roleEntity = RoleDto.mapDtoToEntity(roleDto);
        RoleEntity savedRole = roleRepository.save(roleEntity);
        return RoleDto.mapEntityToDto(savedRole);
    }

    public RoleDto get(Long id) {
        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
        return RoleDto.mapEntityToDto(roleEntity);
    }

    public RoleDto update(RoleDto roleDto, Long id) {
        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
        if (roleDto.getName() != null) {
            roleEntity.setName(roleDto.getName());
        }
        RoleEntity updatedRole = roleRepository.save(roleEntity);
        return RoleDto.mapEntityToDto(updatedRole);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
