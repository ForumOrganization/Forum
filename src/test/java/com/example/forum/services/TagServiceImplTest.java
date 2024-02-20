package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTags_ShouldReturnAllTags() {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(new Tag());
        expectedTags.add(new Tag());

        when(tagRepository.getAllTags()).thenReturn(expectedTags);

        List<Tag> actualTags = tagService.getAllTags();

        assertEquals(expectedTags.size(), actualTags.size());
        for (int i = 0; i < expectedTags.size(); i++) {
            assertEquals(expectedTags.get(i), actualTags.get(i));
        }
    }

    @Test
    void getTagById_ShouldReturnTagById() {
        int tagId = 1;
        Tag expectedTag = new Tag();

        when(tagRepository.getTagById(tagId)).thenReturn(expectedTag);

        Tag actualTag = tagService.getTagById(tagId);

        assertEquals(expectedTag, actualTag);
    }

    @Test
    void getTagById_WhenTagDoesNotExist_ShouldReturnNull() {
        int tagId = 1;

        when(tagRepository.getTagById(tagId)).thenReturn(null);

        Tag actualTag = tagService.getTagById(tagId);

        assertNull(actualTag);
    }

    @Test
    void getTagByName_ShouldReturnTagByName() {
        String tagName = "Test Tag";
        Tag expectedTag = new Tag();

        when(tagRepository.getTagByName(tagName)).thenReturn(expectedTag);

        Tag actualTag = tagService.getTagByName(tagName);

        assertEquals(expectedTag, actualTag);
    }

    @Test
    void getTagByName_WhenTagDoesNotExist_ShouldThrowEntityNotFoundException() {
        String tagName = "Nonexistent Tag";

        when(tagRepository.getTagByName(tagName)).thenThrow(new EntityNotFoundException("Tag", "name", tagName));

        assertThrows(EntityNotFoundException.class, () -> {
            tagService.getTagByName(tagName);
        });
    }

    @Test
    void getAllPostsByTagId_WhenPostsExist_ShouldReturnAllPostsByTagId() {
        int tagId = 1;
        List<Post> expectedPosts = new ArrayList<>();
        expectedPosts.add(new Post());
        expectedPosts.add(new Post());

        when(tagRepository.getAllPostsByTagId(tagId)).thenReturn(expectedPosts);

        List<Post> actualPosts = tagService.getAllPostsByTagId(tagId);

        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    void getAllPostsByTagId_WhenNoPostsExist_ShouldReturnEmptyList() {
        int tagId = 1;
        List<Post> expectedPosts = new ArrayList<>();

        when(tagRepository.getAllPostsByTagId(tagId)).thenReturn(expectedPosts);

        List<Post> actualPosts = tagService.getAllPostsByTagId(tagId);

        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    void createTagInPost_WhenDuplicateExists_ShouldThrowDuplicateEntityException() {
        int postId = 1;
        int authorId = 1;
        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tag.getId())).thenReturn(tag);

        assertThrows(DuplicateEntityException.class, () -> {
            tagService.createTagInPost(tag, postId, user);
        });
    }

    @Test
    void createTagInPost_WhenDuplicateDoesNotExist_ShouldCreateTagInPost() {
        int postId = 1;
        int authorId = 1;
        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tag.getId())).thenReturn(null);
        doNothing().when(tagRepository).createTagInPost(tag, postId, user);

        assertDoesNotThrow(() -> tagService.createTagInPost(tag, postId, user));
    }

    @Test
    void updateTagInPost_WhenDuplicateExists_ShouldThrowDuplicateEntityException() {
        int postId = 1;
        int tagId = 1;
        int authorId = 1;
        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tagId)).thenReturn(tag);

        assertThrows(DuplicateEntityException.class, () -> {
            tagService.updateTagInPost(tag, user, postId, tagId);
        });
    }

    @Test
    void updateTagInPost_WhenDuplicateDoesNotExist_ShouldUpdateTagInPost() {
        int postId = 1;
        int tagId = 1;
        int authorId = 1;
        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tagId)).thenReturn(null);
        doNothing().when(tagRepository).updateTagInPost(tag);

        assertDoesNotThrow(() -> tagService.updateTagInPost(tag, user, postId, tagId));
    }

    @Test
    void deleteTagInPost_WhenDuplicateExists_ShouldThrowDuplicateEntityException() {
        int postId = 1;
        int tagId = 1;
        int authorId = 1;
        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tagId)).thenReturn(tag);

        assertThrows(DuplicateEntityException.class, () -> {
            tagService.deleteTagInPost(tag, user, postId);
        });
    }

    @Test
    void deleteTagInPost_WhenDuplicateDoesNotExist_ShouldDeleteTagInPost() {
        int postId = 1;
        int tagId = 1;
        int authorId = 1;
        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tagId)).thenReturn(null);
        doNothing().when(tagRepository).deleteTagInPost(postId, tagId);

        assertDoesNotThrow(() -> tagService.deleteTagInPost(tag, user, postId));
    }
}