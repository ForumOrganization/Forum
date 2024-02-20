package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forum.utils.CheckPermission.checkAccessPermissions;
import static com.example.forum.utils.CheckPermission.checkAccessPermissionsUser;
import static com.example.forum.utils.Messages.*;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    @Override
    public Tag getTagById(int tagId) {
        return tagRepository.getTagById(tagId);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.getTagByName(name);
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
    public void createTagInPost(Tag tag, int postId, User user) {
        User author = postRepository.getById(postId).getCreatedBy();
        checkAccessPermissionsUser(author.getId(), user, CREATE_TAG_MESSAGE_ERROR);

        boolean duplicateExists = true;

        try {
            Tag existingTag = tagRepository.getTagById(tag.getId());

            if (existingTag.getName().equals(tag.getName())) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Tag", "name", tag.getName());
        }

        this.tagRepository.createTagInPost(tag, postId, user);
    }

    @Override
    public void updateTagInPost(Tag tag, User user, int postId, int tagId) {
        User author = postRepository.getById(postId).getCreatedBy();
        checkAccessPermissions(author.getId(), user, MODIFY_TAG_ERROR_MESSAGE);

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
            throw new DuplicateEntityException("Tag", "id", String.valueOf(tagId));
        }

        this.tagRepository.updateTagInPost(tag);
    }

    @Override
    public void deleteTagInPost(Tag tag, User user, int postId) {
        User author = postRepository.getById(postId).getCreatedBy();
        Tag tagToDelete = tagRepository.getTagById(tag.getId());
        checkAccessPermissions(author.getId(), user, DELETE_TAG_MESSAGE_ERROR);
        this.tagRepository.deleteTagInPost(postId, tagToDelete.getId());
    }
}