package com.vit.community.springapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This DTO holds the subreddit ID, name, description, and number of posts.
 *
 * Lombok library generates the boilerplate code like constructors,
 * getters, setters, equals and hashCode functions at compile time.
 * @Builder is a useful mechanism for using the Builder pattern
 * without writing boilerplate code.
 * */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDto {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
