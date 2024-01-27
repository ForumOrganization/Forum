package com.example.forum.helpers;

import com.example.forum.models.Comment;
import com.example.forum.models.dtos.CommentDto;
import com.example.forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final CommentService commentService;

    @Autowired
    public CommentMapper(CommentService commentService) {
        this.commentService = commentService;
    }


    public Comment fromDto(int id, CommentDto dto) {
        Comment comment = fromDto(dto);
        comment.setId(id);
        comment.setContent(dto.getContent());

        return comment;
    }

    public Comment fromDto(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());


        return comment;
    }
}

