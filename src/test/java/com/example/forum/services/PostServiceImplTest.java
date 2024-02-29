package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.ReactionRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.utils.PostFilterOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private User user;
    private Post post;
    private Tag tag;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setStatus(Status.ACTIVE);

        post = new Post();
        post.setId(1);

        tag = new Tag();
        tag.setId(1);
        tag.setName("Test Tag");
    }

    @Test
    public void getAll_ShouldReturnListOfPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postRepository.getAll(any(PostFilterOptions.class))).thenReturn(posts);

        List<Post> result = postService.getAll(new PostFilterOptions());

        Assertions.assertEquals(posts, result);
    }

    @Test
    public void getTopCommentedPosts_ShouldReturnListOfPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postRepository.getTopCommentedPosts()).thenReturn(posts);

        List<Post> result = postService.getTopCommentedPosts();

        Assertions.assertEquals(posts, result);
    }

    @Test
    public void getMostRecentPosts_ShouldReturnListOfPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postRepository.getMostRecentPosts()).thenReturn(posts);

        List<Post> result = postService.getMostRecentPosts();

        Assertions.assertEquals(posts, result);
    }

    @Test
    public void getById_ShouldReturnPost() {
        when(postRepository.getById(1)).thenReturn(post);

        Post result = postService.getById(1);

        Assertions.assertEquals(post, result);
    }

    @Test
    public void getByTitle_ShouldReturnPost() {
        when(postRepository.getByTitle("Test Title")).thenReturn(post);

        Post result = postService.getByTitle("Test Title");

        Assertions.assertEquals(post, result);
    }

    @Test
    public void create_ShouldThrowAuthorizationException_WhenUserIsBlocked() {
        user.setStatus(Status.BLOCKED);
        Post post = new Post();
        List<Tag> tags = new ArrayList<>();

        Assertions.assertThrows(AuthorizationException.class, () -> postService.create(post, user, tags));

        verify(postRepository, never()).create(post);
    }

    @Test
    public void create_ShouldThrowAuthorizationException_WhenUserIsDeleted() {
        user.setDeleted(true);
        Post post = new Post();
        List<Tag> tags = new ArrayList<>();

        Assertions.assertThrows(AuthorizationException.class, () -> postService.create(post, user, tags));

        verify(postRepository, never()).create(post);
    }
}