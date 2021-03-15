package com.internship.bookstore.service.model;

import com.internship.bookstore.persistence.entity.BookEntity;
import lombok.Data;

/**
 * @author Gurgen Poghosyan
 */
@Data
public class BookWrapper {
    private Long id;
    private String name;

    public BookWrapper(BookEntity entity){
        this.id=entity.getId();
        this.name= entity.getName();
    }
}
