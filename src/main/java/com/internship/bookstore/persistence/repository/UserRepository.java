package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findbyUserName(String username);

    @Query("select u from UserEntity u " +
            "where (:username is null or u.username like concat('%',:username,'%')) and" +
            "(:status is null or u.status = :status) ")
    Page<UserEntity> find(String username, String status, Pageable composePageRequest);
}
