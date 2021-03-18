package com.internship.bookstore.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QueryResponseWrapper<T> {

    private Long total;
    private List<T> data;
}
