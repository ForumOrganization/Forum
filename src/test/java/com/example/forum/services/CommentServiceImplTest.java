package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.CommentRepository;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.utils.CommentFilterOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.forum.helpers.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    CommentRepository mockRepository;
    @Mock
    PostRepository postRepository;
    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    public void get_Should_CallRepository() {
        CommentFilterOptions mockCommentFilterOptions = createMockCommentFilterOptions();

        commentService.getAllCommentsByPostId(1);

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAllCommentsByPostId(1);
    }

    @Test
    public void getCommentById_Should_ReturnComment_When_MatchExists() {
        Mockito.when(mockRepository.getCommentById(1))
                .thenReturn(createMockComment());

        Comment result = commentService.getCommentById(1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("Some content", result.getContent());
    }

    @Test
    public void create_Should_CallRepository_When_CommentNewToPost() {
        Comment comment = createMockComment();
        comment.setId(2);
        postRepository.create(createMockPost());
        User user = createMockUser();

        Mockito.when(mockRepository.getCommentById(comment.getId()))
                .thenThrow(EntityNotFoundException.class);

        commentService.createComment(comment, 1, user);

        Mockito.verify(mockRepository, Mockito.times(1))
                .createComment(comment);
    }

    @Test
    public void create_Should_Throw_When_CommentWithSameIdExists() {
        Comment comment = createMockComment();

        Mockito.lenient().when(mockRepository.getCommentById(comment.getId()))
                .thenReturn(comment);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.createComment(comment, createMockPost().getId(), createMockUser()));
    }

    @Test
    public void update_Should_CallRepository_When_IdIsUniqueAndUserIsAuthorized() {
        Comment comment = createMockComment();
        Comment anotherMockComment = createMockComment();
        anotherMockComment.setId(2);

        Mockito.lenient().when(mockRepository.getCommentById(1))
                .thenReturn(comment);

        Mockito.lenient().when(mockRepository.getCommentById(anotherMockComment.getId()))
                .thenThrow(EntityNotFoundException.class);

        commentService.updateComment(comment, createMockUser());

        Mockito.verify(mockRepository, Mockito.times(1))
                .updateComment(comment);
    }

    @Test
    public void update_Should_Throw_When_UserIsNotCreatorOrAdmin() {
        Comment comment = createMockComment();
        User user = createMockUser();

        comment.setUser(user);
        user.setId(2);

        Mockito.lenient().when(mockRepository.getCommentById(comment.getId()))
                .thenReturn(comment);

        Assertions.assertThrows(AuthorizationException.class,
                () -> commentService.updateComment(comment, createMockUser()));
    }

    @Test
    public void delete_Should_DeleteComment_When_MatchExists() {
        Comment comment = createMockComment();
        User user = createMockUser();

        Mockito.when(mockRepository.getCommentById(1))
                .thenReturn(comment);

        commentService.deleteComment(comment.getId(), user);

        Mockito.verify(mockRepository, Mockito.times(1))
                .deleteComment(comment.getId(), createMockUser());
    }

    @Test
    public void delete_Should_Throw_When_CommentDoesNotExist() {
        Comment comment = createMockComment();
        User user = createMockUser();
        comment.setUser(user);
        user.setId(2);

        Mockito.when(mockRepository.getCommentById(1))
                .thenReturn(comment);

        Assertions.assertThrows(AuthorizationException.class,
                () -> commentService.deleteComment(comment.getId(), createMockUser()));
    }
}