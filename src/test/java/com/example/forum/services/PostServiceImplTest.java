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
    void update_ValidPostAndUser_UpdatesPost() {
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

}
