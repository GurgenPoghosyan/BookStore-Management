package com.internship.bookstore.service.model;

import com.internship.bookstore.persistence.entity.AuthorEntity;
import lombok.Data;

/**
 * @author Gurgen Poghosyan
 */

@Data
public class AuthorWrapper {
    private Long id;
    private String name;

    public AuthorWrapper(AuthorEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
