package com.internship.bookstore.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gurgen Poghosyan
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {

    private String username;
    private String password;
}
