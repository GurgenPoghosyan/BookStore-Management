package com.internship.bookstore.service.criteria;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gurgen Poghosyan
 */

@Getter
@Setter
public class UserDetailsSearchCriteria extends SearchCriteria {

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String emailAddress;
}
