package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.models.enums.Reaction;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.PostRepositoryImpl;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.ReactionRepository;
import com.example.forum.services.PostServiceImpl;
import com.example.forum.utils.PostFilterOptions;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void create_post_when_valid_user_is_present() {
//        Post post = new Post();
//        User user = new User();
//        int postId = post.getId(); // Assuming postId is 1
//        Post existingPost = new Post();
//        existingPost.setCreatedBy(user);
//        when(postRepository.getById(postId)).thenReturn(existingPost);
//        assertDoesNotThrow(() -> postService.create(post, user));
//        verify(postRepository, times(1)).create(post);
//        assertEquals(user, post.getCreatedBy());
//    }


//    @Test
//    void update_post_when_valid_user_is_present() {
//        Post post = new Post();
//        post.setId(1);
//        User user = new User();
//        user.setId(1);
//        post.setCreatedBy(user);
//
//        when(postRepository.getById(post.getId())).thenReturn(post);
//
//        assertDoesNotThrow(() -> {
//            postService.update(post, user);
//        });
//
//        verify(postRepository, times(1)).update(post);
//    }


    @Test
    void update_post_when_user_is_blocked_test() {
        Post post = new Post();
        User user = new User();
        user.setStatus(Status.BLOCKED);

        assertThrows(AuthorizationException.class, () -> {
            postService.update(post, user,new Tag());
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

    @Test
    public void testGetAll() {
        PostFilterOptions filterOptions = new PostFilterOptions();
        List<Post> expectedPosts = Arrays.asList(
                new Post(),
                new Post()
        );

        when(postRepository.getAll(filterOptions)).thenReturn(expectedPosts);
        List<Post> actualPosts = postService.getAll(filterOptions);

        assertEquals(expectedPosts.size(), actualPosts.size());
        assertEquals(expectedPosts.get(0).getTitle(), actualPosts.get(0).getTitle());
        assertEquals(expectedPosts.get(1).getContent(), actualPosts.get(1).getContent());

        verify(postRepository, times(1)).getAll(filterOptions);
    }


    @Test
    public void testGetTopCommentedPosts() {

        List<Post> expectedTopCommentedPosts = Arrays.asList(
                new Post(),
                new Post()
        );

        when(postRepository.getTopCommentedPosts()).thenReturn(expectedTopCommentedPosts);

        List<Post> actualTopCommentedPosts = postService.getTopCommentedPosts();

        assertEquals(expectedTopCommentedPosts.size(), actualTopCommentedPosts.size());
        assertEquals(expectedTopCommentedPosts.get(0).getTitle(), actualTopCommentedPosts.get(0).getTitle());
        assertEquals(expectedTopCommentedPosts.get(1).getContent(), actualTopCommentedPosts.get(1).getContent());

        verify(postRepository, times(1)).getTopCommentedPosts();
    }

    @Test
    public void testGetMostRecentPosts() {
        List<Post> expectedMostRecentPosts = Arrays.asList(
                new Post(),
                new Post()
        );

        when(postRepository.getMostRecentPosts()).thenReturn(expectedMostRecentPosts);

        List<Post> actualMostRecentPosts = postService.getMostRecentPosts();

        assertEquals(expectedMostRecentPosts.size(), actualMostRecentPosts.size());
        assertEquals(expectedMostRecentPosts.get(0).getTitle(), actualMostRecentPosts.get(0).getTitle());
        assertEquals(expectedMostRecentPosts.get(1).getContent(), actualMostRecentPosts.get(1).getContent());

        verify(postRepository, times(1)).getMostRecentPosts();
    }

    @Test
    public void testGetById() {

        Post expectedPost = new Post();

        when(postRepository.getById(1)).thenReturn(expectedPost);

        Post actualPost = postService.getById(1);

        assertEquals(expectedPost.getId(), actualPost.getId());
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        assertEquals(expectedPost.getContent(), actualPost.getContent());

        verify(postRepository, times(1)).getById(1);
    }

    @Test
    public void testGetByTitle() {

        String sampleTitle = "Sample Title";
        Post expectedPost = new Post();

        when(postRepository.getByTitle(sampleTitle)).thenReturn(expectedPost);

        Post actualPost = postService.getByTitle(sampleTitle);

        assertEquals(expectedPost.getId(), actualPost.getId());
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        assertEquals(expectedPost.getContent(), actualPost.getContent());

        verify(postRepository, times(1)).getByTitle(sampleTitle);
    }
    @Test
    public void testGetByComment() {

        int sampleCommentId = 123;

        Post expectedPost = new Post();

        when(postRepository.getByComment(sampleCommentId)).thenReturn(expectedPost);

        Post actualPost = postService.getByComment(sampleCommentId);

        assertEquals(expectedPost.getId(), actualPost.getId());
        assertEquals(expectedPost.getTitle(), actualPost.getTitle());
        assertEquals(expectedPost.getContent(), actualPost.getContent());

        verify(postRepository, times(1)).getByComment(sampleCommentId);
    }

}
