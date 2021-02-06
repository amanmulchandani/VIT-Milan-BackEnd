package com.vit.community.springapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
 * Corresponds to the subreddit table in the database.
 *
 * Lombok library generates the boilerplate code like constructors,
 * getters, setters, equals and hashCode functions at compile time.
 * @Builder is a useful mechanism for using the Builder pattern
 * without writing boilerplate code.
 *
 * Categorizes similar types of posts under a single category (subreddit).
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Subreddit {
//    Primary Key
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

//    Subreddit name cannot be blank. Displays error message if it is blank.
    @NotBlank(message = "Community name is required")
    @Column(unique = true)
    private String name;

//    Description cannot be blank. Displays error message if it is blank.
    @NotBlank(message = "Description is required")
    private String description;

//    One subreddit can have multiple posts (List of posts)
    @OneToMany(fetch = LAZY)
    private List<Post> posts;

//    Time at which subreddit is created
    private Instant createdDate;

//    The user who has created this subreddit.
//    One subreddit can only be created by one user. But one user can create multiple subreddits.
    @ManyToOne(fetch = LAZY)
    private User user;
}
