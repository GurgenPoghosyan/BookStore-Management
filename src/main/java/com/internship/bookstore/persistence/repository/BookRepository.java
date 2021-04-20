package com.internship.bookstore.persistence.repository;

import com.internship.bookstore.persistence.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Gurgen Poghosyan
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    BookEntity findByName(String name);

    @Query(value = "select * from books b " +
            "inner join books_authors ba on ba.book_id = b.id " +
            "inner join authors a on a.id = ba.author_id " +
            "inner join books_genres bg on b.id = bg.book_id " +
            "inner join genres g on bg.genre_id = g.id " +
            "inner join publishers p on b.publisher_id = p.id " +
            "where (:name is null or b.book_name like concat('%',:name,'%') and " +
            "(:minRating is null or b.rating >= :minRating) and " +
            "(:startDate is null or b.date_of_publication >= :startDate)) and " +
            "(:endDate is null or b.date_of_publication <= :endDate) and " +
            "(:minPageSize is null or b.num_pages >= :minPageSize) and " +
            "(:maxPageSize is null or b.num_pages <= :maxPageSize) and " +
            "(:authorName is null or a.author_name like concat('%',:authorName,'%')) and " +
            "(:genreName is null or g.genre like concat('%',:genreName,'%')) and " +
            "(:publisherName is null or p.publisher_name like concat('%',:publisherName,'%'))", nativeQuery = true)
    Page<BookEntity> find(String name,
                          Double minRating,
                          String startDate,
                          String endDate,
                          Integer minPageSize,
                          Integer maxPageSize,
                          String authorName,
                          String genreName,
                          String publisherName,
                          Pageable pageable);
}
