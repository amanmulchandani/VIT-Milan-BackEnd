package com.vit.community.springapplication.repository;

import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.model.Subreddit;
import com.vit.community.springapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
