package com.vit.community.springapplication.service;

import com.vit.community.springapplication.dto.CommentsDto;
import com.vit.community.springapplication.exceptions.PostNotFoundException;
import com.vit.community.springapplication.exceptions.SpringCommunityException;
import com.vit.community.springapplication.mapper.CommentMapper;
import com.vit.community.springapplication.model.Comment;
import com.vit.community.springapplication.model.NotificationEmail;
import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.model.User;
import com.vit.community.springapplication.repository.CommentRepository;
import com.vit.community.springapplication.repository.PostRepository;
import com.vit.community.springapplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

/*
* The CommentService class is responsible for saving and retrieving comments
* to and from the database using the CommentRepository class.
* Also sends an email notification to the user whenever another user
* comments on their post using MailService class.
* */

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    /*
    * Uses the post repository to retrieve the post on which the comment is created, the authService
    * to retrieve the current logged in user, and the commentMapper to map the CommentsDto to a
    * Comment object and finally saves it into the database using the commentRepository.
    *
    * Additionally sends a notification email to the creator of the post regarding the comment.
    * */

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        User currentUser = authService.getCurrentUser();
        Comment comment = commentMapper.map(commentsDto, post, currentUser);
        commentRepository.save(comment);

        sendCommentNotification(post.getUser(), currentUser);
    }

    public void deleteAllCommentsForPost(Post post){
        List<Comment> comments = commentRepository.findByPost(post);
        for(Comment tempComment : comments) {
            commentRepository.delete(tempComment);
        }
    }

    /* Uses the existing MailService class to send the email. */

    private void sendCommentNotification(User postCreator, User currentUser) {
        String message = mailContentBuilder.build(currentUser.getUsername() + " posted a comment on your post." + POST_URL);
        mailService.sendMail(new NotificationEmail(currentUser.getUsername() + " commented on your post", postCreator.getEmail(), message));
    }

    /*
    * Retrieves all the comments created on a post with given postId as parameter from the database,
    * maps them to CommentsDto and sends them back to the controller as a list of CommentsDto-s.
    * */

    @Transactional(readOnly = true)
    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    /*
     * Retrieves all the comments created by a user with given username as parameter from the database,
     * maps them to CommentsDto and sends them back to the controller as a list of CommentsDto-s.
     * */

    @Transactional(readOnly = true)
    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new SpringCommunityException(id.toString()));
        commentRepository.delete(comment);
    }
}
