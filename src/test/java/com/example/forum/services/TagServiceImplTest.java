package com.example.forum.services;
import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.TagRepositoryImpl;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.services.TagServiceImpl;
import com.example.forum.utils.TagFilterOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.times;

public class TagServiceImplTest {

    @Mock
    private TagRepositoryImpl tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postRepository = mock(PostRepository.class);
        tagService = new TagServiceImpl(tagRepository, postRepository);
    }

    @Test
    void get_all_tags_with_no_filters() {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(new Tag());
        expectedTags.add(new Tag());

        when(tagRepository.getAllTags(null)).thenReturn(expectedTags);

        List<Tag> actualTags = tagService.getAllTags(null);

        assertEquals(expectedTags.size(), actualTags.size());
        for (int i = 0; i < expectedTags.size(); i++) {
            assertEquals(expectedTags.get(i), actualTags.get(i));
        }
    }
    @Test
    void getTagById_test() {
        int tagId = 1;
        Tag expectedTag = new Tag();

        when(tagRepository.getTagById(tagId)).thenReturn(expectedTag);

        Tag actualTag = tagService.getTagById(tagId);

        assertEquals(expectedTag, actualTag);
    }

    @Test
    void getTagById_when_Tag_does_not_exists() {
        int tagId = 1;

        when(tagRepository.getTagById(tagId)).thenReturn(null);

        Tag actualTag = tagService.getTagById(tagId);

        assertEquals(null, actualTag);
    }

    @Test
    void getTagByName_when_Tag_does_not_exists() {
        String tagName = "Test Tag";
        Tag expectedTag = new Tag();

        when(tagRepository.getTagByName(tagName)).thenReturn(expectedTag);

        Tag actualTag = tagService.getTagByName(tagName);

        assertEquals(expectedTag, actualTag);
    }

    @Test
    void getTagByName_when_tag_does_not_exists() {
        String tagName = "Nonexistent Tag";

        when(tagRepository.getTagByName(tagName)).thenThrow(new EntityNotFoundException("Tag", "name", tagName));

        assertThrows(EntityNotFoundException.class, () -> {
            tagService.getTagByName(tagName);
        });
    }

    @Test
    void getAllPostsByTagId_when_posts_exist() {
        int tagId = 1;
        List<Post> expectedPosts = new ArrayList<>();
        expectedPosts.add(new Post());
        expectedPosts.add(new Post());

        when(tagRepository.getAllPostsByTagId(tagId)).thenReturn(expectedPosts);

        List<Post> actualPosts = tagService.getAllPostsByTagId(tagId);

        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    void getAllPostsByTagId_when_no_posts_exist() {
        int tagId = 1;
        List<Post> expectedPosts = new ArrayList<>();

        when(tagRepository.getAllPostsByTagId(tagId)).thenReturn(expectedPosts);

        List<Post> actualPosts = tagService.getAllPostsByTagId(tagId);

        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    void test_CreateTagInPost() {
        int postId = 1;
        int authorId = 1;
        User user = new User(); // Simulate authorized user
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        doNothing().when(tagRepository).createTagInPost(tag, postId, user);

        assertDoesNotThrow(() -> tagService.createTagInPost(tag, postId, user));
    }
    @Test
    void testUpdateTagInPost() {


        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        int postId = post.getId();
        Tag tag = new Tag();
        int tagId = tag.getId();
        int authorId = tagId;
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tagId)).thenReturn(tag);
        doNothing().when(tagRepository).updateTagInPost(tag);
        assertDoesNotThrow(() -> tagService.updateTagInPost(tag, user, postId, tagId));
    }
//
//    @Test
//    void testUpdateTagInPost_AuthorizationException() {
//        // Arrange
//        int postId = 1;
//        int tagId = 1;
//        int authorId = 2; // Different from the user ID
//        User user = new User(3, "user", "password"); // Simulate authorized user
//        Post post = new Post(postId, "Title", "Content", new User(authorId, "author", "password"));
//        Tag tag = new Tag(tagId, "Tag");
//        when(postRepository.getById(postId)).thenReturn(post);
//
//        // Act & Assert
//        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> tagService.updateTagInPost(tag, user, postId, tagId));
//        assertEquals("Authorization failed", exception.getMessage());
//    }
//
//    @Test
//    void testUpdateTagInPost_TagNotFoundException() {
//        // Arrange
//        int postId = 1;
//        int tagId = 1;
//        User user = new User(1, "user", "password"); // Simulate authorized user
//        when(tagRepository.getTagById(tagId)).thenThrow(new EntityNotFoundException("Tag", tagId));
//
//        // Act & Assert
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> tagService.updateTagInPost(new Tag(tagId, "Tag"), user, postId, tagId));
//        assertEquals("Tag not found with id: " + tagId, exception.getMessage());
//    }
//
//    @Test
//    void testUpdateTagInPost_PostNotFoundException() {
//        // Arrange
//        int postId = 1;
//        int tagId = 1;
//        User user = new User(1, "user", "password"); // Simulate authorized user
//        when(postRepository.getById(postId)).thenThrow(new EntityNotFoundException("Post", postId));
//
//        // Act & Assert
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> tagService.updateTagInPost(new Tag(tagId, "Tag"), user, postId, tagId));
//        assertEquals("Post not found with id: " + postId, exception.getMessage());
//    }
//
//    @Test
//    void testUpdateTagInPost_DuplicateEntityException() {
//        // Arrange
//        int postId = 1;
//        int tagId = 1;
//        int authorId = 1;
//        User user = new User(authorId, "user", "password"); // Simulate authorized user
//        Post post = new Post(postId, "Title", "Content", user);
//        Tag tag = new Tag(tagId, "Tag");
//        when(postRepository.getById(postId)).thenReturn(post);
//        when(tagRepository.getTagById(tagId)).thenReturn(tag);
//
//        // Act & Assert
//        DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> tagService.updateTagInPost(tag, user, postId, tagId));
//        assertEquals("Tag with name 'Tag' already exists", exception.getMessage());
//    }

}
