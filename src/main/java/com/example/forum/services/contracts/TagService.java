package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.utils.TagFilterOptions;

import java.util.List;

public interface TagService {

    List<Tag> getAllTags();

    Tag getTagById(int tagId);

    Tag getTagByName(String name);

    List<Post> getAllPostsByTagId(int tagId);

    List<Post> getAllPostsByTagName(String tagName);

    List<Tag> getAllTagsByPostId(int postId);

    void createTagInPost(Tag tag, int postId, User user);

    void updateTagInPost(Tag tag, User user, int postId, int tagId);

    void deleteTagInPost(Tag tag,User user, int postId);
}