package com.vit.community.springapplication.repository;

import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.model.User;
import com.vit.community.springapplication.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

    List<Vote> findByPost(Post post);
}
