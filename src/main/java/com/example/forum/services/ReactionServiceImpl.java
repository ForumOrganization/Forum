package com.example.forum.services;

import com.example.forum.models.Reaction;
import com.example.forum.services.contracts.ReactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReactionServiceImpl implements ReactionService {


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