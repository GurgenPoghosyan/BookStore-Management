package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.UserDetailsEntity;
import com.internship.bookstore.service.dto.UserDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {

    @Query("select ud from UserDetailsEntity ud " +
            "where (:firstName is null or ud.firstName like concat('%',:firstName,'%') )" +
            "and (:lastName is null or ud.lastName like concat('%',:lastName,'%'))" +
            "and (:address is null or ud.address like concat('%',:address,'%')) " +
            "and (:phoneNumber is null or ud.phoneNumber like concat('%',:phoneNumber,'%')) " +
            "and (:emailAddress is null or ud.emailAddress like concat('%',:emailAddress,'%') )")
    Page<UserDetailsEntity> find(String firstName,
                              String lastName,
                              String address,
                              String phoneNumber,
                              String emailAddress,
                              Pageable pageable);
}
