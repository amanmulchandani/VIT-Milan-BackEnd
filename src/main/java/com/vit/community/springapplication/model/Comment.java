package com.vit.community.springapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
 * Corresponds to the comment table in the database.
 *
 * Lombok library generates the boilerplate code like constructors,
 * getters, setters, equals and hashCode functions at compile time.
 *
 * Stores the comments created by users on posts.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
//    Primary Key
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

//    The content cannot be empty.
    @NotEmpty
    private String text;

//    The post on which this comment is created.
//    One comment belongs to only one post. But a post can have multiple comments.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

//    Time at which the comment was created.
    private Instant createdDate;

//    The user who has created this comment.
//    One comment can be created by only one user. But a user can create multiple comments.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}
