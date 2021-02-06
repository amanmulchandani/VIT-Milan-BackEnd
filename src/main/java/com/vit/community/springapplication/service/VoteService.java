package com.vit.community.springapplication.service;

import com.vit.community.springapplication.dto.VoteDto;
import com.vit.community.springapplication.exceptions.PostNotFoundException;
import com.vit.community.springapplication.exceptions.SpringCommunityException;
import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.model.User;
import com.vit.community.springapplication.model.Vote;
import com.vit.community.springapplication.repository.PostRepository;
import com.vit.community.springapplication.repository.VoteRepository;
import com.vit.community.springapplication.model.VoteType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
* Contains logic for distinguishing the type of vote casted by user and
* saving it into the database. Only allows one type of vote per user per post.
* */

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    /*
    * Retrieves currently logged in user and post on which user has casted the vote.
    * Retrieves the latest vote casted by that user on that post.
    * If latest vote type exists and is same as current casted vote type, then throws
    * an error, else sets the voteCount on that post accordingly and saves the vote
    * and post into the database.
    * */

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        User currentUser = authService.getCurrentUser();
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, currentUser);
        if (voteByPostAndUser.isPresent()){
            if(voteByPostAndUser.get().getVoteType()
                    .equals(voteDto.getVoteType())) {
                throw new SpringCommunityException("You have already "
                        + voteDto.getVoteType() + "'d for this post");
            }
            else if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
                post.setVoteCount(post.getVoteCount() + 2);
            } else {
                post.setVoteCount(post.getVoteCount() - 2);
            }
        }
        else if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post, currentUser));
        postRepository.save(post);
    }

    /* Maps the VoteDto to Vote object for saving into the database */

    private Vote mapToVote(VoteDto voteDto, Post post, User currentUser) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(currentUser)
                .build();
    }

    public void deleteAllVotesForPost(Post post){
        List<Vote> votes = voteRepository.findByPost(post);
        for(Vote tempVote : votes){
            voteRepository.delete(tempVote);
        }
    }
}
