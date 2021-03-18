package com.internship.bookstore.transform.requestbody.book;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class BookUpdateRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String date;
}
