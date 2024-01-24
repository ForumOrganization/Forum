package com.example.forum.services;

import com.example.forum.models.Comment;
import com.example.forum.services.contracts.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public List<Comment> getAllCommentsByPostId(int postId) {
        return null;
    }

    @Override
    public void createComment(Comment comment, int postId) {

    }

    @Override
    public void updateComment(Comment comment, int postId) {

    }

    @Override
    public void deleteComment(int commentId) {

    }
}