package com.internship.bookstore.transform.requestbody.collection;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class AddBookToCollectionRequest {
    @NotEmpty
    private Long collectionId;

    @NotEmpty
    private Long bookId;
}
