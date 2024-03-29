package com.example.forum.services.contracts;

import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.User;

import java.util.List;

public interface ReactionService {

    List<Reaction_posts> getAllReactionsByPostId(int postId);

    List<Reaction_comments> getAllReactionsByCommentId(int commentId);

    void updateReactionPost(Reaction_posts reaction, int postId);

    void deleteReactionPost(int reactionId, User user);

    void updateReactionComment(Reaction_comments reaction, int commentId);

    Reaction_comments findReactionByCommentIdAndUserId(int commentId, int userId);

    Reaction_posts findReactionByPostIdAndUserId(int postId, int userId);
}