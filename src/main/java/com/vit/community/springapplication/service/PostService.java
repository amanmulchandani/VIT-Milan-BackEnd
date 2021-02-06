package com.vit.community.springapplication.service;

import com.vit.community.springapplication.dto.PostRequest;
import com.vit.community.springapplication.dto.PostResponse;
import com.vit.community.springapplication.exceptions.PostNotFoundException;
import com.vit.community.springapplication.exceptions.SubredditNotFoundException;
import com.vit.community.springapplication.mapper.PostMapper;
import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.model.Subreddit;
import com.vit.community.springapplication.model.User;
import com.vit.community.springapplication.repository.PostRepository;
import com.vit.community.springapplication.repository.SubredditRepository;
import com.vit.community.springapplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

/*
 * The PostService class is responsible for saving and retrieving the posts
 * to and from the database using the subredditRepository.
 * */

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final VoteService voteService;
    private final AuthService authService;
    private final PostMapper postMapper;

    /*
    * Retrieves the subreddit with which the PostRequest is associated from the database,
    * and the user who has created the PostRequest, and finally uses the PostMapper to
    * build the Post object from above objects and saves it into the database.
    * */

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    public void save(PostRequest postRequest, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));

        post.setDescription(postRequest.getDescription());
        post.setPostName(postRequest.getPostName());
        postRepository.save(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
//        Delete comments
        commentService.deleteAllCommentsForPost(post);
//        Delete votes
        voteService.deleteAllVotesForPost(post);
//        Delete Post
        postRepository.delete(post);
    }

    /*
    * Retrieves the Post with the given id from the database, maps it to a PostResponse
    * using the PostMapper class and sends it back to the controller.
    * */

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    /*
    * Retrieves all the posts created from the database, maps them to PostResponse
    * and returns them as a list of PostResponses.
    * */

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    /*
     * Retrieves all the posts associated with the given subreddit id from the database,
     * maps them to PostResponse and returns them as a list of PostResponses.
     * */

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    /*
     * Retrieves all the posts created by a user with given username from the database,
     * maps them to PostResponse and returns them as a list of PostResponses.
     * */

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
