package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.BookEntity;
import com.internship.bookstore.service.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    BookEntity findByName(String name);

    @Query("select b from BookEntity b " +
            "where (:name is null or b.name like concat('%',:name,'%') and " +
            "(:minRating is null or b.rating >= :minRating) and " +
            "(:startDate is null or b.date >= :startDate)) and " +
            "(:endDate is null or b.date <= :endDate) and " +
            "(:minPageSize is null or b.pages >= :minPageSize) and " +
            "(:maxPageSize is null or b.pages <= :maxPageSize)")
    Page<BookEntity> find(String name,
                          Double minRating,
                          String startDate,
                          String endDate,
                          Integer minPageSize,
                          Integer maxPageSize,
                          Pageable pageable);
}
