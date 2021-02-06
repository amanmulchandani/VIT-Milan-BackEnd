package com.vit.community.springapplication.controller;

import com.vit.community.springapplication.dto.SubredditDto;
import com.vit.community.springapplication.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * The SubredditController class handles all the HTTP GET/POST requests related
 * to creating subreddits and reading all existing subreddits.
 * Delegates calls to SubredditService calls and returns a ResponseEntity object
 * with the appropriate HTTP status and body (data).
 *
 * The Lombok library generates the all arguments constructor, and the fields
 * are automatically autowired (initialised) by Spring at runtime.
 *
 * */

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    /*
    * The POST API request to create a subreddit containing the subreddit DTO in the request body.
    * */

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subredditService.save(subredditDto));
    }

    /*
    * The GET API request to read all existing subreddits in the database.
    * */

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getAll());
    }

    /*
    * The GET API request to read the subreddit from database with the id
    * provided in the URL path.
    * */

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getSubreddit(id));
    }
}
