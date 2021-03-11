package com.internship.bookstore.transform.request.book;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class BookCreateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String date;

    @NotEmpty
    private String genreName;

    @NotEmpty
    private Long authorId;

}
