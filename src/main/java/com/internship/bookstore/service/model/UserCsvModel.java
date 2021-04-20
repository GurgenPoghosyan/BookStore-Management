package com.internship.bookstore.service.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Gurgen Poghosyan
 */

@Getter
@Setter
public class UserCsvModel {

    private Long id;

    @CsvBindByName(column = "User Name")
    private String username;

    @CsvBindByName(column = "Password;;;")
    private String password;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "E Mail")
    private String email;

    @CsvBindByName(column = "Phone No.")
    private String phone;

    @CsvBindByName(column = "County")
    private String country;

    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "State")
    private String state;

    @CsvBindByName(column = "Zip")
    private String zip;
}
