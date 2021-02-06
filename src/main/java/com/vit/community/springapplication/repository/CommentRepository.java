package com.vit.community.springapplication.repository;

import com.vit.community.springapplication.model.Comment;
import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
