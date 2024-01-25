package com.example.forum.services;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.services.contracts.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;


    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return null;
    }

    @Override
    public List<Post> getAllPostsByTagId(int tagId) {
        return tagRepository.getAllPostsByTagId(tagId);
    }

    @Override
    public List<Post> getAllPostsByTagName(String tagName) {
        return this.tagRepository.getAllPostsByTagName(tagName);
    }

    @Override
    public List<Tag> getAllTagsByPostId(int postId) {
        return null;
    }

    @Override
    public Tag getTagById(int tagId) {
        return tagRepository.getTagById(tagId);
    }

    @Override
    public void createTagInPost(Tag tag, int postId, User user) {
        //TODO Check if the user has permission to create a tag in this post
        this.tagRepository.createTagInPost(tag, postId, user);
    }

    @Override
    public void updateTagInPost(int tagId, User user) {

    }

    @Override
    public void deleteTagInPost(int tagId, User user) {

    }
}