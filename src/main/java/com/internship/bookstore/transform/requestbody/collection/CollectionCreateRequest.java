package com.internship.bookstore.transform.requestbody.collection;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class CollectionCreateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String genreName;

}
