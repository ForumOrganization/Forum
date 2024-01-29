package com.example.forum.repositories.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.utils.TagFilterOptions;

import java.util.List;

public interface TagRepository {

    List<Tag> getAllTags(TagFilterOptions tagFilterOptions);
    Tag getTagById(int tagId);
    Tag getTagByName(String name);

    List<Post> getAllPostsByTagId(int tagId);

    List<Post> getAllPostsByTagName(String tagName);

    List<Tag> getAllTagsByPostId(int postId);

    void createTagInPost(Tag tag, int postId, User user);

    void updateTagInPost(Tag tag);

    void deleteTagInPost(int tagId);
}