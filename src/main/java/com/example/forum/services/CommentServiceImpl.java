package com.example.forum.services;

import com.example.forum.models.Comment;
import com.example.forum.repositories.contracts.CommentRepository;
import com.example.forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getAllCommentsByPostId(int postId) {
        return this.commentRepository.getAllCommentsByPostId(postId);
    }

    @Override
    public void createComment(Comment comment, int postId) {

    }

    @Override
    public void updateComment(Comment comment, int postId) {

    }

    @Override
    public void deleteComment(int commentId) {
        this.commentRepository.deleteComment(commentId);
    }
}