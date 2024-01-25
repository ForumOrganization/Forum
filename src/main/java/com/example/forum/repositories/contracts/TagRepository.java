package com.example.forum.repositories.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;

import java.util.List;

public interface TagRepository {

    List<Tag> getAllTags();

    List<Post> getAllPostsByTagId(int tagId);

    List<Post> getAllPostsByTagName(String tagName);

    List<Tag> getAllTagsByPostId(int postId);

    Tag getTagById(int tagId);

    void createTagInPost(Tag tag, int postId, User user);

    void updateTagInPost(Tag tag);

    void deleteTagInPost(int tagId);
}