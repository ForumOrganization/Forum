package com.example.forum.repositories.contracts;

import com.example.forum.models.Comment;
import com.example.forum.models.User;
import com.example.forum.utils.CommentFilterOptions;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAllCommentsByPostId(int postId, CommentFilterOptions commentFilterOptions);

    public Comment getCommentById(int commentId);

    void createComment(Comment comment);

    void updateComment(Comment comment);

    void deleteComment(int commentId, User user);
}