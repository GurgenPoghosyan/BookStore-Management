package com.internship.bookstore.transform.request.collection;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class CollectionUpdateRequset {

    @NotEmpty
    private String name;

}
