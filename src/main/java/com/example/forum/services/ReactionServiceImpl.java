package com.example.forum.services;

import com.example.forum.repositories.contracts.ReactionRepository;
import com.example.forum.services.contracts.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReactionServiceImpl implements ReactionService {

    private ReactionRepository reactionRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public List<Reaction> getAllReactionsByPostId(Reaction reaction, int postId) {
        return reactionRepository.getAllReactionsByPostId(reaction, postId);
    }

    @Override
    public List<Reaction> getAllReactionsByCommentId(Reaction reaction, int commentId) {
        return null;
    }

    @Override
    public Map<Reaction, Integer> countReactionsPost(int postId) {
        return reactionRepository.countReactionsPost(postId);
    }

    @Override
    public Map<Reaction, Integer> countReactionsComment(int commentId) {
        return reactionRepository.countReactionsPost(commentId);
    }

    @Override
    public void updateReactionPost(Reaction reaction, int postId) {

    }

    @Override
    public void updateReactionComment(Reaction reaction, int commentId) {
        reactionRepository.updateReactionComment(reaction, commentId);
    }
}