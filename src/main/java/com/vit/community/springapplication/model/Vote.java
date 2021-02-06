package com.vit.community.springapplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/*
 * Corresponds to the vote table in the database.
 *
 * Lombok library generates the boilerplate code like constructors,
 * getters, setters, equals and hashCode functions at compile time.
 * @Builder is a useful mechanism for using the Builder pattern
 * without writing boilerplate code.
 *
 * Stores the votes casted by users on posts.
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Vote {
//    Primary Key
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long voteId;

//    Distinguishes whether the vote is an upvote or a downvote.
    private VoteType voteType;

//    The post on which this vote is cast.
//    One vote is associated with exactly one post. A post can have multiple votes.
    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

//    The user who has cast this vote.
//    One vote is cast by exactly one user. A user vote multiple posts.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}
