package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.CommentRepository;
import com.example.forum.repositories.contracts.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setStatus(Status.ACTIVE);

        post = new Post();
        post.setId(1);

        comment = new Comment();
        comment.setId(1);
        comment.setUser(user);
        comment.setPost(post);
    }

    @Test
    public void getAllCommentsByPostId_ShouldReturnListOfComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.getAllCommentsByPostId(1)).thenReturn(comments);

        List<Comment> result = commentService.getAllCommentsByPostId(1);

        Assertions.assertEquals(comments, result);
    }

    @Test
    public void getCommentById_ShouldReturnComment() {
        when(commentRepository.getCommentById(1)).thenReturn(comment);

        Comment result = commentService.getCommentById(1);

        Assertions.assertEquals(comment, result);
    }

    @Test
    public void createComment_ShouldCreateComment() {
        Comment newComment = new Comment();
        newComment.setContent("New Comment");

        when(postRepository.getById(1)).thenReturn(post);

        commentService.createComment(newComment, 1, user);

        verify(commentRepository, times(1)).createComment(any(Comment.class));
    }

    @Test
    public void updateComment_ShouldUpdateComment() {
        Comment updatedComment = new Comment();
        updatedComment.setId(1);
        updatedComment.setContent("Updated Comment");
        updatedComment.setUser(user);

        when(commentRepository.getCommentById(1)).thenReturn(comment);

        commentService.updateComment(updatedComment, user);

        verify(commentRepository, times(1)).updateComment(any(Comment.class));
    }

    @Test
    public void deleteComment_ShouldDeleteComment() {
        when(commentRepository.getCommentById(1)).thenReturn(comment);

        commentService.deleteComment(1, user);

        verify(commentRepository, times(1)).deleteComment(1, user);
    }

    @Test
    public void createComment_ShouldThrowAuthorizationException_WhenUserIsBlocked() {
        user.setStatus(Status.BLOCKED);
        Comment comment = new Comment();
        int postId = 1;

        Assertions.assertThrows(AuthorizationException.class, () -> commentService.createComment(comment, postId, user));

        verify(commentRepository, never()).createComment(comment);
    }

    @Test
    public void createComment_ShouldThrowAuthorizationException_WhenUserIsDeleted() {
        user.setDeleted(true);
        Comment comment = new Comment();
        int postId = 1;

        Assertions.assertThrows(AuthorizationException.class, () -> commentService.createComment(comment, postId, user));

        verify(commentRepository, never()).createComment(comment);
    }

    @Test
    public void createComment_ShouldCreateComment_WhenUserIsActive() {
        Comment comment = new Comment();
        int postId = 1;

        Assertions.assertDoesNotThrow(() -> commentService.createComment(comment, postId, user));

        verify(commentRepository, times(1)).createComment(comment);
    }
}