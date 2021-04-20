package com.internship.bookstore.service.criteria;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gurgen Poghosyan
 */

@Getter
@Setter
public class BookSearchCriteria extends SearchCriteria {

    private String name;

    private String startDate;

    private String endDate;

    private Double minRating;

    private Integer minPageSize;

    private Integer maxPageSize;

    private String authorName;

    private String genreName;

    private String publisherName;
}
