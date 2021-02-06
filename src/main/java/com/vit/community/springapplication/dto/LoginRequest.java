package com.vit.community.springapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* The LoginRequest DTO encapsulates the username and password that is
* entered by the user during login.
*
* Lombok library generates the boilerplate code like constructors,
* getters, setters, equals and hashCode functions at compile time.
* */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
}
