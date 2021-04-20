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

    UserEntity findByUsername(String username);

    @Query(value = "select * from users u " +
            "inner join user_details ud on ud.id = u.details_id " +
            "inner join roles r on r.id = u.role_id " +
            "inner join users_communities uc on uc.user_id = u.id " +
            "inner join community c on c.id = uc.community_id " +
            "where (:username is null or u.username like concat('%',:username,'%')) and" +
            "(:status is null or u.status = :status) and " +
            "(:firstName is null or ud.first_name like concat('%',:firstName,'%')) and " +
            "(:lastName is null or ud.last_name like concat('%',:lastName,'%')) and " +
            "(:role is null or r.name like concat('%',:role,'%')) and " +
            "(:zipCode is null or c.zip_code like concat('%',:zipCode,'%'))", nativeQuery = true)
    Page<UserEntity> find(String username,
                          String status,
                          String firstName,
                          String lastName,
                          String role,
                          String zipCode,
                          Pageable composePageRequest);
}
