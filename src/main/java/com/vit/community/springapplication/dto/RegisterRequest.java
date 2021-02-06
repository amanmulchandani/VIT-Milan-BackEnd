package com.vit.community.springapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* The RegisterRequest class is a Data Access Object (DTO) which holds
* the user registration details like email, username and password
* entered by them on the registration page.
*
* Lombok library generates the boilerplate code like constructors,
* getters, setters, equals and hashCode functions at compile time.*/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String username;
    private String password;

}
