package com.example.forum.repositories.contracts;

import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;

import java.util.List;
import java.util.Map;

public interface ReactionRepository {

    List<Reaction_posts> getAllReactionsByPostId(Reaction_posts reaction, int postId);

    List<Reaction_comments> getAllReactionsByCommentId(Reaction_comments reaction, int commentId);

    Map<Reaction_posts, Integer> countReactionsPost(int postId);

    Map<Reaction_comments, Integer> countReactionsComment(int commentId);

    void updateReactionPost(Reaction_posts reaction, int postId);

    void updateReactionComment(Reaction_comments reaction, int commentId);
}