package com.example.forum.services;

import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.enums.Reaction;
import com.example.forum.repositories.contracts.ReactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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

//    @Test
//    void countReactionsPost_ShouldReturnReactionCountMap() {
//        int postId = 1;
//        Map<Reaction_posts, Integer> expectedReactionCountMap = Map.of(new Reaction_posts(), 2);
//
//        when(reactionRepository.countReactionsPost(postId)).thenReturn(expectedReactionCountMap);
//
//        Map<Reaction_posts, Integer> actualReactionCountMap = reactionService.countReactionsPost(postId);
//
//        assertEquals(expectedReactionCountMap, actualReactionCountMap);
//    }
//
//    @Test
//    void countReactionsComment_ShouldReturnReactionCountMap() {
//        int commentId = 1;
//        Map<Reaction, Integer> expectedReactionCountMap = Map.of(new Reaction(), 3);
//
//        when(reactionRepository.countReactionsComment(commentId)).thenReturn(expectedReactionCountMap);
//
//        Map<Reaction_comments, Integer> actualReactionCountMap = reactionService.countReactionsComment(commentId);
//
//        assertEquals(expectedReactionCountMap, actualReactionCountMap);
//    }

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
}