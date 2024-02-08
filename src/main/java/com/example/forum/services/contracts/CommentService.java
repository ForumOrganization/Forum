package com.example.forum.services.contracts;

import com.example.forum.models.Comment;
import com.example.forum.models.User;
import com.example.forum.utils.CommentFilterOptions;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsByPostId(int postId);

    Comment getCommentById(int commentId);

    void createComment(Comment comment, int postId, User user);

    void updateComment(Comment comment, User user);

    void deleteComment(int commentId, User user);
}