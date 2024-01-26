package com.example.forum.services;

import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
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
    public List<Reaction_posts> getAllReactionsByPostId(Reaction_posts reaction, int postId) {
        return reactionRepository.getAllReactionsByPostId(reaction, postId);
    }

    @Override
    public List<Reaction_comments> getAllReactionsByCommentId(int commentId) {
        return reactionRepository.getAllReactionsByCommentId(commentId);
    }

    @Override
    public Map<Reaction_posts, Integer> countReactionsPost(int postId) {
        return reactionRepository.countReactionsPost(postId);
    }

    @Override
    public Map<Reaction_comments, Integer> countReactionsComment(int commentId) {
        return reactionRepository.countReactionsComment(commentId);
    }

    @Override
    public void updateReactionPost(Reaction_posts reaction, int postId) {

    }

    @Override
    public void updateReactionComment(Reaction_comments reaction, int commentId) {
        reactionRepository.updateReactionComment(reaction, commentId);
    }
}