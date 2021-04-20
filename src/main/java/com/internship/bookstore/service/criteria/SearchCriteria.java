package com.internship.bookstore.service.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Getter
@Setter
public class SearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page;
    private Integer size;

    private String sortField;
    private String sortDirection;

    public Pageable composePageRequest() {
        if (size != null && size == Integer.MAX_VALUE) {
            return null;
        }

        int page = this.page == null ? 0 : this.page;
        int size = this.size == null ? 20 : this.size;

        sortField = this.sortField == null ? "id" : this.sortField;
        Sort.Direction sortDirection = this.sortDirection == null ? Sort.Direction.ASC : Sort.Direction.valueOf(this.sortDirection.toUpperCase());

        return PageRequest.of(page, size, Sort.by(sortDirection, sortField));
    }
}
