package com.internship.bookstore.service.criteria;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gurgen Poghosyan
 */
@Getter
@Setter
public class CommunitySearchCriteria extends SearchCriteria {

    private String name;

    private String zip;
}
