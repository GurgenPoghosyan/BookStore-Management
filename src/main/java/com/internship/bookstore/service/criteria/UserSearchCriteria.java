package com.internship.bookstore.service.criteria;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gurgen Poghosyan
 */
@Getter
@Setter
public class UserSearchCriteria extends SearchCriteria{

    private String username;

    private String status;

}
