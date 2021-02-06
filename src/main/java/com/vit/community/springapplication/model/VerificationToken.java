package com.vit.community.springapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
 * Corresponds to the token table in the database.
 * Lombok library generates the boilerplate code like constructors,
 * getters, setters, equals and hashCode functions at compile time.
 *
 * Whenever a user registers on the website, a token is generated
 * and stored in the database, and also sent as part of the activation
 * link to the user via email. Whenever the user clicks on that link,
 * the user associated with the token is validated in the database
 * and enabled.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class VerificationToken {
//  Primary Key
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

//    Verification token generated for email activation
    private String token;

//    The user with which the token is associated.
//    One user will have one verification token, and vice-versa.
    @OneToOne(fetch = LAZY)
    private User user;

//    Time at which the token will expire
    private Instant expiryDate;
}
