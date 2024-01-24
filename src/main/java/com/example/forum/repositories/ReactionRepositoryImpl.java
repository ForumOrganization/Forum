package com.example.forum.repositories;

import com.example.forum.models.Reaction;
import com.example.forum.repositories.contracts.ReactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReactionRepositoryImpl implements ReactionRepository {


    @Override
    public List<Reaction> getAllReactionsByPostId(Reaction reaction, int postId) {
        return null;
    }

    @Override
    public List<Reaction> getAllReactionsByCommentId(Reaction reaction, int commentId) {
        return null;
    }

    @Override
    public Map<Reaction, Integer> countReactionsPost() {
        return null;
    }

    @Override
    public Map<Reaction, Integer> countReactionsComment() {
        return null;
    }

    @Override
    public void updateReactionPost(Reaction reaction, int postId) {

    }

    @Override
    public void updateReactionComment(Reaction reaction, int commentId) {

    }
}