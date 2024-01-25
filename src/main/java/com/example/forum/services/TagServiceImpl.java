package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forum.utils.CheckPermission.checkAccessPermissionsUser;
import static com.example.forum.utils.Messages.MODIFY_TAG_ERROR_MESSAGE;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
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
        return this.tagRepository.getAllTagsByPostId(postId);
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
    public void updateTagInPost(Tag tag, User user) {
        checkAccessPermissionsUser(tag.getId(), user, MODIFY_TAG_ERROR_MESSAGE);
        boolean duplicateExists = true;
        try {
            Tag existingTag = tagRepository.getTagById(tag.getId());
            if (existingTag.getId() == tag.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Tag", "name", tag.getName());
        }
        this.tagRepository.updateTagInPost(tag);

    }

    @Override
    public void deleteTagInPost(int tagId, User user) {

    }
}