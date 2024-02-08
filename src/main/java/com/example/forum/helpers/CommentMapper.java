package com.example.forum.helpers;

import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.CommentDto;
import com.example.forum.services.contracts.PostService;
import com.example.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentMapper(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    public Comment fromDto(int id, CommentDto dto) {
        Comment comment = fromDto(dto);
        comment.setId(id);
        comment.setContent(dto.getContent());
        Post post = postService.getByComment(id);
        comment.setPost(post);
        User user = userService.getUserByComment(id);
        comment.setUser(user);
        return comment;
    }

    public Comment fromDto(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(dto.getUser());
        return comment;
    }
    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        return commentDto;
    }
}