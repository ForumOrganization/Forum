package com.example.forum.services;

import com.example.forum.models.Comment;
import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.User;
import com.example.forum.models.enums.Reaction;
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
    public List<Reaction_posts> getAllReactionsByPostId(int postId) {
        return reactionRepository.getAllReactionsByPostId(postId);
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
        reactionRepository.updateReactionPost(reaction, postId);
    }

    @Override
    public void updateReactionComment(Reaction_comments reaction, int commentId) {
        reactionRepository.updateReactionComment(reaction, commentId);
    }
//    @Override
//    public void likeComment(Comment comment, User user) {
//        Reaction_comments reaction = reactionRepository.f(comment.getId(), user.getId());
//        if (reaction == null) {
//            reaction = new Reaction_comments();
//            reaction.setComment(comment);
//            reaction.setUser(user);
//        }
//        reaction.setReaction(Reaction.LIKES);
//        updateReactionComment(reaction,comment.getId());
//        comment.getReactions().add(reaction);
//
//    }
//
//    @Override
//    public void dislikeComment(Comment comment, User user) {
//        Reaction_comments reaction = findReactionByCommentIdAndUserId(comment.getId(), user.getId());
//        if (reaction == null) {
//            reaction = new Reaction_comments();
//            reaction.setComment(comment);
//            reaction.setUser(user);
//        }
//        reaction.setReaction(Reaction.DISLIKES);
//        updateReactionComment(reaction,comment.getId());
//    }
}
