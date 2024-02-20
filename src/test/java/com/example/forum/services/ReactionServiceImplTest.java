package com.example.forum.services;

import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.ReactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReactionServiceImplTest {

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private ReactionServiceImpl reactionService;

    @Test
    void getAllReactionsByPostId_ShouldReturnReactionsList() {
        int postId = 1;
        List<Reaction_posts> expectedReactions = List.of(new Reaction_posts(), new Reaction_posts());

        when(reactionRepository.getAllReactionsByPostId(postId)).thenReturn(expectedReactions);

        List<Reaction_posts> actualReactions = reactionService.getAllReactionsByPostId(postId);

        assertEquals(expectedReactions, actualReactions);
    }

    @Test
    void getAllReactionsByCommentId_ShouldReturnReactionsList() {
        int commentId = 1;
        List<Reaction_comments> expectedReactions = List.of(new Reaction_comments(), new Reaction_comments());

        when(reactionRepository.getAllReactionsByCommentId(commentId)).thenReturn(expectedReactions);

        List<Reaction_comments> actualReactions = reactionService.getAllReactionsByCommentId(commentId);

        assertEquals(expectedReactions, actualReactions);
    }

    @Test
    void updateReactionPost_ShouldCallRepository() {
        Reaction_posts reaction = new Reaction_posts();
        int postId = 1;

        reactionService.updateReactionPost(reaction, postId);

        Mockito.verify(reactionRepository, times(1))
                .updateReactionPost(reaction, postId);
    }

    @Test
    void updateReactionComment_ShouldCallRepository() {
        Reaction_comments reaction = new Reaction_comments();
        int commentId = 1;

        reactionService.updateReactionComment(reaction, commentId);

        Mockito.verify(reactionRepository, times(1)).
                updateReactionComment(reaction, commentId);
    }

    @Test
    public void testDeleteReactionPost() {

        int reactionId = 123;
        User user = new User();
        doNothing().when(reactionRepository).deleteReactionPost(reactionId, user);

        reactionService.deleteReactionPost(reactionId, user);

        verify(reactionRepository, times(1)).deleteReactionPost(reactionId, user);
    }

    @Test
    public void testFindReactionByCommentIdAndUserId() {

        int commentId = 123;
        int userId = 456;
        Reaction_comments expectedReaction = new Reaction_comments();


        when(reactionRepository.findReactionByCommentIdAndUserId(commentId, userId)).thenReturn(expectedReaction);

        Reaction_comments actualReaction = reactionService.findReactionByCommentIdAndUserId(commentId, userId);
        assertEquals(expectedReaction, actualReaction);
        verify(reactionRepository, times(1)).findReactionByCommentIdAndUserId(commentId, userId);
    }

    @Test
    public void testFindReactionByPostIdAndUserId() {

        int postId = 123;
        int userId = 456;
        Reaction_posts expectedReaction = new Reaction_posts();
        when(reactionRepository.findReactionByPostIdAndUserId(postId, userId)).thenReturn(expectedReaction);

        Reaction_posts actualReaction = reactionService.findReactionByPostIdAndUserId(postId, userId);

        assertEquals(expectedReaction, actualReaction);

        verify(reactionRepository, times(1)).findReactionByPostIdAndUserId(postId, userId);
    }
}