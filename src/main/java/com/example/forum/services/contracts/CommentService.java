package com.example.forum.services.contracts;

import com.example.forum.models.Comment;

import java.util.List;

public interface CommentService {

    //TODO Yoana
    List<Comment> getAllCommentsByPostId(int postId);

    //TODO Veronika
    void createComment(Comment comment, int postId);

    //TODO Siyana
    void updateComment(Comment comment, int postId);

    //TODO Yoana
    void deleteComment(int commentId);
}