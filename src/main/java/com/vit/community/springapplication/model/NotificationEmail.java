package com.vit.community.springapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* This class encapsulates the email subject, recipient, and body
* in a single object.
*
* Lombok library generates the boilerplate code like constructors,
* getters, setters, equals and hashCode functions at compile time.
*
* */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
