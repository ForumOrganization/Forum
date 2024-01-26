package com.example.forum.services.contracts;

import java.util.List;
import java.util.Map;

public interface ReactionService {

    //TODO Veronika - Done
    List<Reaction> getAllReactionsByPostId(Reaction reaction, int postId);

    //TODO Siyana
    List<Reaction> getAllReactionsByCommentId(Reaction reaction, int commentId);

    //TODO Yoana
    Map<Reaction, Integer> countReactionsPost(int postId);

    //TODO Veronika - Done
    Map<Reaction, Integer> countReactionsComment(int commentId);

    //TODO Siyana
    void updateReactionPost(Reaction reaction, int postId);

    //TODO Yoana
    void updateReactionComment(Reaction reaction, int commentId);
}