package com.example.forum.services.contracts;

import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;

import java.util.List;
import java.util.Map;

public interface ReactionService {

    //TODO Veronika - Done
    List<Reaction_posts> getAllReactionsByPostId(Reaction_posts reaction, int postId);

    //TODO Siyana
    List<Reaction_comments> getAllReactionsByCommentId(Reaction_comments reaction, int commentId);

    //TODO Yoana
    Map<Reaction_posts, Integer> countReactionsPost(int postId);

    //TODO Veronika - Done
    Map<Reaction_comments, Integer> countReactionsComment(int commentId);

    //TODO Siyana
    void updateReactionPost(Reaction_posts reaction, int postId);

    //TODO Yoana
    void updateReactionComment(Reaction_comments reaction, int commentId);
}