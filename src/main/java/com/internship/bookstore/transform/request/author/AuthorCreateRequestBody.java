package com.internship.bookstore.transform.request.author;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class AuthorCreateRequestBody {
    @NotEmpty
    private String name;
}
