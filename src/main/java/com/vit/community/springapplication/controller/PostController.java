package com.vit.community.springapplication.controller;

import com.vit.community.springapplication.dto.PostRequest;
import com.vit.community.springapplication.dto.PostResponse;
import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

/*
* The PostController handles all the HTTP GET/POST requests from client related to
* creating posts and reading existing posts by id, subreddit and username.
* Delegates calls to PostService class and returns a ResponseEntity object with the
* appropriate HTTP status and body (data).
* */

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    /*
    * The POST API call for creating a post and saving it into the database.
    * Contains the PostRequest DTO as part of the request body of the call.
    * */

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> editPost(@RequestBody PostRequest postRequest, @PathVariable Long id){
        postService.save(postRequest, id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* The GET API call to read all the posts from the database. */

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    /*
    * The GET API call to read the post with the post id provided as part
    * of the URL path.
    * */

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    /*
     * The GET API call to read all posts associated with the given subreddit id
     * provided as part of the URL path.
     * */

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    /*
     * The GET API call to read all posts created by the user with username
     * provided as part of the URL path.
     * */

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String name) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }
}
