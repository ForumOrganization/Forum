package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;

import java.util.List;
import java.util.Set;

public interface TagService {

    //TODO Veronika - Done
    List<Tag> getAllTags();
    //TODO Siyana - Done
    List<Post> getAllPostsByTagId(int tagId);

    //TODO Yoana
    List<Post> getAllPostsByTagName(String tagName);

    //TODO Veronika - Done
    List<Tag> getAllTagsByPostId(int postId);
    //TODO Siyana - Done
    Tag getTagById(int tagId);

    //TODO Yoana
    void createTagInPost(Tag tag, int postId, User user);

    //TODO Veronika - Done
    void updateTagInPost(Tag tag, User user);
    //TODO Siyana - Done

    void deleteTagInPost(int tagId, User user);
}