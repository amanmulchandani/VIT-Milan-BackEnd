package com.vit.community.springapplication.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.vit.community.springapplication.dto.CommentsDto;
import com.vit.community.springapplication.model.Comment;
import com.vit.community.springapplication.model.Post;
import com.vit.community.springapplication.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "duration", expression = "java(getDuration(comment))")
    CommentsDto mapToDto(Comment comment);

    default String getDuration(Comment comment) {
        return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }
}