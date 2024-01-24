package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;

import java.util.List;
import java.util.Set;

public interface TagService {

    //TODO Veronika
    List<Tag> getAllTags();

    //TODO Siyana
    List<Post> getAllPostsByTagId(int tagId);

    //TODO Yoana
    List<Post> getAllPostsByTagName(String tagName);

    //TODO Veronika
    List<Tag> getAllTagsByPostId(int postId);

    //TODO Siyana
    Tag getTagById(int tagId);

    //TODO Yoana
    void createTagInPost(Tag tag, int postId, User user);

    //TODO Veronika
    void updateTagInPost(int tagId, User user);

    //TODO Siyana
    void deleteTagInPost(int tagId, User user);
}