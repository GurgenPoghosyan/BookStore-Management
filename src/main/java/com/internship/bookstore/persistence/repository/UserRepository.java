package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Gurgen Poghosyan
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
