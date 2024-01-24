package com.example.forum.services.contracts;

import com.example.forum.models.Reaction;

import java.util.List;
import java.util.Map;

public interface ReactionService {

    //TODO Veronika
    List<Reaction> getAllReactionsByPostId(Reaction reaction, int postId);

    //TODO Siyana
    List<Reaction> getAllReactionsByCommentId(Reaction reaction, int commentId);

    //TODO Yoana
    Map<Reaction, Integer> countReactionsPost();

    //TODO Veronika
    Map<Reaction, Integer> countReactionsComment();

    //TODO Siyana
    void updateReactionPost(Reaction reaction, int postId);

    //TODO Yoana
    void updateReactionComment(Reaction reaction, int commentId);
}