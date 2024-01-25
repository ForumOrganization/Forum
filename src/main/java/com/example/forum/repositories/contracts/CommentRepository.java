package com.example.forum.repositories.contracts;

import com.example.forum.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAllCommentsByPostId(int postId);

    public Comment getCommentById(int commentId);

    void createComment(Comment comment, int postId);

    void updateComment(Comment comment);

    void deleteComment(int commentId);
}