package com.example.forum.services;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.repositories.TagRepositoryImpl;
import com.example.forum.services.TagServiceImpl;
import com.example.forum.utils.TagFilterOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TagServiceImplTest {

    @Mock
    private TagRepositoryImpl tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

}
