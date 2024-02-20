package com.example.forum.services;

import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.ReactionRepository;
import com.example.forum.services.contracts.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public List<Reaction_posts> getAllReactionsByPostId(int postId) {
        return reactionRepository.getAllReactionsByPostId(postId);
    }

    @Override
    public List<Reaction_comments> getAllReactionsByCommentId(int commentId) {
        return reactionRepository.getAllReactionsByCommentId(commentId);
    }

    @Override
    public void updateReactionPost(Reaction_posts reaction, int postId) {
        reactionRepository.updateReactionPost(reaction, postId);
    }

    @Override
    public void updateReactionComment(Reaction_comments reaction, int commentId) {
        reactionRepository.updateReactionComment(reaction, commentId);
    }

    @Override
    public void deleteReactionPost(int reactionId, User user) {
        reactionRepository.deleteReactionPost(reactionId, user);
    }

    public Reaction_comments findReactionByCommentIdAndUserId(int commentId, int userId) {
        return reactionRepository.findReactionByCommentIdAndUserId(commentId, userId);
    }

    public Reaction_posts findReactionByPostIdAndUserId(int postId, int userId) {
        return reactionRepository.findReactionByPostIdAndUserId(postId, userId);
    }
}
