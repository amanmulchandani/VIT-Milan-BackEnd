package com.vit.community.springapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/*
* This class encapsulates the Authentication token details and username as a response object
* which is sent to the client when they successfully login to the server.
*
* Lombok library generates the boilerplate code like constructors,
* getters, setters, equals and hashCode functions at compile time.
*
* */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String username;
}
