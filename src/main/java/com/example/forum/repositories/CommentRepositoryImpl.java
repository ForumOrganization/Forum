package com.example.forum.repositories;

import com.example.forum.models.Comment;
import com.example.forum.repositories.contracts.CommentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
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