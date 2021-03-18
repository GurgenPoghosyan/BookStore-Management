package com.internship.bookstore.transform.requestbody.book;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class AddAuthorRequest {
    @NotEmpty
    private Long authorId;
}
