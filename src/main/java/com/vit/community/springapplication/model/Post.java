package com.vit.community.springapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
* Corresponds to the post table in the database.
*
* Lombok library generates the boilerplate code like constructors,
* getters, setters, equals and hashCode functions at compile time.
* @Builder is a useful mechanism for using the Builder pattern
* without writing boilerplate code.
*
* Stores all the details of a post, the votes on that post,
* the creator of that post, the subreddit (sub-category) to which the
* post belongs to, etc.
* */

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
//    Primary Key
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;

//    Post Name cannot be blank. Displays error message if it is blank.
    @NotBlank(message = "Post Name cannot be empty or Null")
    @Column(unique = true)
    private String postName;

//    URL can be null. Flags a warning if dereferenced without checking for null.
    @Nullable
    private String url;

//    Lob: Persisted as Large Object to database-supported large object type (BLOB)
    @Nullable
    @Lob
    private String description;

//    Used to keep track of votes on the post
    private Integer voteCount = 0;

//    The user who has created this post.
//    One post is associated with only one user. But one user can have multiple posts
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

//    Time at which the post is created
    private Instant createdDate;

//    The subreddit to which this post belongs to.
//    One post belongs to only one subreddit. But one subreddit can have multiple posts.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Subreddit subreddit;
}
