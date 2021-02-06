package com.vit.community.springapplication.controller;

import com.vit.community.springapplication.dto.CommentsDto;
import com.vit.community.springapplication.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/*
 * The PostController handles all the HTTP GET/POST requests from client related to
 * creating comments and reading comments on posts or by user.
 * Delegates calls to CommentService class and returns a ResponseEntity object with the
 * appropriate HTTP status and body (data).
 * */

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    /*
     * The POST API call for creating a comment on a post and saving it into the database.
     * Contains the Comments DTO as part of the request body of the call.
     * */

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.save(commentsDto);
        return new ResponseEntity<>(CREATED);
    }

    /*
    * The GET API call for reading all comments created on a post with given postID
    * as part of the URL path.
    * */

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    /*
     * The GET API call for reading all comments created by a user with given username
     * as part of the URL path.
     * */

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return new ResponseEntity<>(OK);
    }

}
