package com.example.forum.services.contracts;

import com.example.forum.models.Comment;
import com.example.forum.models.User;

import java.util.List;

public interface CommentService {

    //TODO Yoana
    List<Comment> getAllCommentsByPostId(int postId);

    //TODO Veronika
    void createComment(Comment comment, int postId);

    void updateComment(Comment comment, User user);

    //TODO Yoana
    void deleteComment(int commentId);
}