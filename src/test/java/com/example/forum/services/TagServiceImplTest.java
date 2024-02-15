package com.example.forum.services;
import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import static org.mockito.Mockito.*;
import com.example.forum.repositories.TagRepositoryImpl;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.services.TagServiceImpl;
import com.example.forum.services.contracts.TagService;
import com.example.forum.utils.TagFilterOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        User user = new User();
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



    @Test
    void testUpdateTagInPost_when_post_does_not_exists() {
        int postId = 1;
        int tagId = 1;
        User user = new User();
        when(postRepository.getById(postId)).thenThrow(new EntityNotFoundException("Post", postId));

    }

    @Test
    void testUpdateTagInPost_DuplicateEntityException() {
        int postId = 1;
        int tagId = 1;
        int authorId = 1;
        User user = new User();
        Post post = new Post();
        post.setCreatedBy(user);
        Tag tag = new Tag();
        when(postRepository.getById(postId)).thenReturn(post);
        when(tagRepository.getTagById(tagId)).thenReturn(tag);

    }

    @Test
    public void testGetAllPostsByTagName() {
        // Define sample data
        String tagName = "exampleTag";
        List<Post> expectedPosts = Arrays.asList(
                new Post(),
                new Post()
        );

        // Mock the behavior of tagRepository.getAllPostsByTagName() method
        when(tagRepository.getAllPostsByTagName(tagName)).thenReturn(expectedPosts);

        // Call the method under test
        List<Post> actualPosts = tagService.getAllPostsByTagName(tagName);

        // Verify that the method behaves as expected
        assertEquals(expectedPosts.size(), actualPosts.size());
        assertEquals(expectedPosts.get(0).getTitle(), actualPosts.get(0).getTitle());
        assertEquals(expectedPosts.get(1).getContent(), actualPosts.get(1).getContent());
    }

    @Test
    public void testGetAllTagsByPostId() {
        // Define sample data
        int postId = 123;
        List<Tag> expectedTags = Arrays.asList(
                new Tag(),
                new Tag()
        );

        // Mock the behavior of tagRepository.getAllTagsByPostId() method
        when(tagRepository.getAllTagsByPostId(postId)).thenReturn(expectedTags);

        // Call the method under test
        List<Tag> actualTags = tagService.getAllTagsByPostId(postId);

        // Verify that the method behaves as expected
        assertEquals(expectedTags.size(), actualTags.size());
        assertEquals(expectedTags.get(0).getName(), actualTags.get(0).getName());
        assertEquals(expectedTags.get(1).getName(), actualTags.get(1).getName());
    }
}
