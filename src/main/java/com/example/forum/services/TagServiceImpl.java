package com.example.forum.services;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.services.contracts.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Override
    public List<Tag> getAllTags() {
        return null;
    }

    @Override
    public List<Post> getAllPostsByTagId(int tagId) {
        return null;
    }

    @Override
    public List<Post> getAllPostsByTagName(String tagName) {
        return null;
    }

    @Override
    public List<Tag> getAllTagsByPostId(int postId) {
        return null;
    }

    @Override
    public Tag getTagById(int tagId) {
        return null;
    }

    @Override
    public void createTagInPost(Tag tag, int postId, User user) {

    }

    @Override
    public void updateTagInPost(int tagId, User user) {

    }

    @Override
    public void deleteTagInPost(int tagId, User user) {

    }
}