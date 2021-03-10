package com.internship.bookstore.transform.request.book;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class AddAuthorRequest {
    @NotEmpty
    private Long bookId;

    @NotEmpty
    private Long authorId;
}
