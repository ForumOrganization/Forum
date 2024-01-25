package com.example.forum.repositories.contracts;

import com.example.forum.models.Reaction;

import java.util.List;
import java.util.Map;

public interface ReactionRepository {

    List<Reaction> getAllReactionsByPostId(Reaction reaction, int postId);

    List<Reaction> getAllReactionsByCommentId(Reaction reaction, int commentId);

    Map<Reaction, Integer> countReactionsPost(int postId);

    Map<Reaction, Integer> countReactionsComment(int commentId);

    void updateReactionPost(Reaction reaction, int postId);

    void updateReactionComment(Reaction reaction, int commentId);
}