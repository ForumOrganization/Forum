package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.PostRepositoryImpl;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.services.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_post_when_valid_user_is_present() {
        Post post = new Post();
        User user = new User();

        assertDoesNotThrow(() -> {
            postService.create(post, user);
        });

        verify(postRepository, times(1)).create(post);
        assertEquals(user, post.getCreatedBy());
    }
    @Test
    void update_post_when_valid_user_is_present() {
        Post post = new Post();
        post.setId(1);
        User user = new User();
        user.setId(1);
        post.setCreatedBy(user);

        when(postRepository.getById(post.getId())).thenReturn(post);

        assertDoesNotThrow(() -> {
            postService.update(post, user);
        });

        verify(postRepository, times(1)).update(post);
    }


    @Test
    void update_post_when_user_is_blocked_test() {
        Post post = new Post();
        User user = new User();
        user.setStatus(Status.BLOCKED);

        assertThrows(AuthorizationException.class, () -> {
            postService.update(post, user);
        });

        verify(postRepository, never()).update(post);
    }

    @Test
    void delete_post_when_valid_user_is_present() {
        int postId = 1;
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(postId);
        post.setCreatedBy(user);

        when(postRepository.getById(postId)).thenReturn(post);

        assertDoesNotThrow(() -> {
            postService.delete(postId, user);
        });

        verify(postRepository, times(1)).delete(postId);
    }

    @Test
    void delete_check_when_post_is_not_found() {
        int postId = 1;
        User user = new User();
        user.setId(1);

        when(postRepository.getById(postId)).thenThrow(new EntityNotFoundException("Post", "id", String.valueOf(postId)));

        assertThrows(EntityNotFoundException.class, () -> {
            postService.delete(postId, user);
        });

        verify(postRepository, never()).delete(postId);
    }

    @Test
    void delete_when_user_is_not_authorized() {
        int postId = 1;
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(postId);
        post.setCreatedBy(new User()); // Different user created the post

        when(postRepository.getById(postId)).thenReturn(post);

        assertThrows(AuthorizationException.class, () -> {
            postService.delete(postId, user);
        });

        verify(postRepository, never()).delete(postId);
    }

    @Test
    void delete_when_user_is_blocked() {
        int postId = 1;
        User user = new User();
        user.setId(1);
        user.setStatus(Status.BLOCKED);

        assertThrows(AuthorizationException.class, () -> {
            postService.delete(postId, user);
        });

        verify(postRepository, never()).delete(postId);
    }

    @Test
    void delete_when_user_is_deleted() {
        int postId = 1;
        User user = new User();
        user.setId(1);
        user.setDeleted(true);

        assertThrows(AuthorizationException.class, () -> {
            postService.delete(postId, user);
        });

        verify(postRepository, never()).delete(postId);
    }

}
