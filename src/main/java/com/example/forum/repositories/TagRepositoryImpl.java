package com.example.forum.repositories;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.repositories.contracts.TagRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
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
    public void createTagInPost(Tag tag, int postId) {

    }

    @Override
    public void updateTagInPost(int tagId) {

    }

    @Override
    public void deleteTagInPost(int tagId) {

    }
}