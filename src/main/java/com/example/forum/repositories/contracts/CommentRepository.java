package com.example.forum.repositories.contracts;

import com.example.forum.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAllCommentsByPostId(int postId);

    void createComment(Comment comment, int postId);

    void updateComment(Comment comment, int postId);

    void deleteComment(int commentId);
}