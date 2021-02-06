package com.vit.community.springapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

/*
 * Corresponds to the user table in the database.
 *
 * Lombok library generates the boilerplate code like constructors,
 * getters, setters, equals and hashCode functions at compile time.
 *
 * Stores the user credentials like username, password, email, etc.
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
//    Primary Key
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

//    Username cannot be blank. Displays error message if it is blank.
    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private String username;

//    Password cannot be blank. Displays error message if it is blank.
    @NotEmpty(message = "Password is required")
    private String password;

//    @Email validates whether the entered text is a valid email address.
//    Cannot be empty. Displays error message if it is empty.
    @Email
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

//    Time at which user is created
    private Instant created;

//    True if registered user has completed the email verification process (activated).
    private boolean enabled;
}
