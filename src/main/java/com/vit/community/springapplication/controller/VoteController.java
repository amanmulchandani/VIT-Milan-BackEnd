package com.vit.community.springapplication.controller;

import com.vit.community.springapplication.dto.VoteDto;
import com.vit.community.springapplication.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* The VoteController class is responsible handling the HTTP Post request from
* client for casting votes on posts and delegates the functionality implementation
* to the VoteService class.
* */

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    /* The POST API call for casting an UPVOTE or a DOWNVOTE on a post.
    * Receives the VoteDto object as part of the request body.
    * */

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}