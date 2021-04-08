package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Gurgen Poghosyan
 */
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

    Optional<RoleEntity> findByName(String name);
}
