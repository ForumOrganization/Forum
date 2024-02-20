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
    public void create_ShouldCreatePost() {
        Post newPost = new Post();
        newPost.setTitle("New Post Title");
        newPost.setContent("New Post Content");

        List<Tag> tags = new ArrayList<>();
        tags.add(tag);

        postService.create(newPost, user, tags);

        verify(postRepository, times(1)).create(newPost);
        verify(tagRepository, times(1)).createTagInPost(tag, newPost.getId(), user);
    }

    @Test
    public void update_ShouldUpdatePost() {
        Post updatedPost = new Post();
        updatedPost.setId(1);
        updatedPost.setTitle("Updated Post Title");
        updatedPost.setContent("Updated Post Content");

        List<Tag> newTags = new ArrayList<>();
        newTags.add(tag);

        when(postRepository.getById(1)).thenReturn(post);

        postService.update(updatedPost, user, newTags);

        verify(postRepository, times(1)).update(updatedPost);
        verify(tagRepository, times(1)).createTagInPost(tag, updatedPost.getId(), user);
    }

    @Test
    public void delete_ShouldDeletePost() {
        when(postRepository.getById(1)).thenReturn(post);

        postService.delete(1, user);

        verify(postRepository, times(1)).delete(1);
    }

    @Test
    public void reactToPost_ShouldReactToPost() {
        Post currentPost = new Post();
        currentPost.setId(1);

        when(postRepository.getById(1)).thenReturn(currentPost);

        postService.reactToPost(1, new Reaction_posts());

        verify(reactionRepository, times(1)).updateReactionPost(any(Reaction_posts.class), 1);
    }

    @Test
    public void countReactionLikes_ShouldReturnCorrectCount() {
        Post post = new Post();
        post.setId(1);
        post.setReactions(new HashSet<>());
        post.getReactions().add(new Reaction_posts());

        long count = postService.countReactionLikes(post);

        Assertions.assertEquals(1, count);
    }

    @Test
    public void countReactionDislikes_ShouldReturnCorrectCount() {
        Post post = new Post();
        post.setId(1);
        post.setReactions(new HashSet<>());
        post.getReactions().add(new Reaction_posts());

        long count = postService.countReactionDislikes(post);

        Assertions.assertEquals(1, count);
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

    @Test
    public void create_ShouldCreatePost_WhenUserIsActive() {
        Post post = new Post();
        List<Tag> tags = new ArrayList<>();

        Assertions.assertDoesNotThrow(() -> postService.create(post, user, tags));

        verify(postRepository, times(1)).create(any(Post.class));
    }
}