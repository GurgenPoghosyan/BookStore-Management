package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.CommunityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {

    @Query("select c from CommunityEntity c " +
            "where (:name is null or c.name like concat('%',:name,'%')) and " +
            "(:zipCode is null or c.zip like concat('%',:zipCode,'%'))")
    Page<CommunityEntity> find(String name, String zipCode, Pageable pageable);

    CommunityEntity findByNameAndZip(String name, String zip);
}
