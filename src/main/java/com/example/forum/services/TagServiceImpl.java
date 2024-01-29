package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.services.contracts.TagService;
import com.example.forum.utils.TagFilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forum.utils.CheckPermission.checkAccessPermissions;
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
    public List<Tag> getAllTags(TagFilterOptions tagFilterOptions) {
        return tagRepository.getAllTags(tagFilterOptions);
    }

    @Override
    public Tag getTagById(int tagId) {
//        Tag tag = tagRepository.getTagById(tagId);
//        if (tag == null) {
//            throw new EntityNotFoundException("Tag", "tagId", String.valueOf(tagId));
//        }
        return tagRepository.getTagById(tagId);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.getTagByName(name);
    }

    @Override
    public List<Post> getAllPostsByTagId(int tagId) {
        Tag tag = tagRepository.getTagById(tagId);
        if (tag == null) {
            throw new EntityNotFoundException("Tag", "id", String.valueOf(tagId));
        }
        return tagRepository.getAllPostsByTagId(tagId);
    }

    @Override
    public List<Post> getAllPostsByTagName(String tagName) {
        Tag tag = tagRepository.getTagByName(tagName);
        if (tag == null) {
            throw new EntityNotFoundException("Tag", "name", tagName);
        }
        return this.tagRepository.getAllPostsByTagName(tagName);
    }

    @Override
    public List<Tag> getAllTagsByPostId(int postId) {
        Post post = postRepository.getById(postId);
        if (post == null) {
            throw new EntityNotFoundException("Post", "id", String.valueOf(postId));
        }
        return this.tagRepository.getAllTagsByPostId(postId);
    }

    @Override
    public void createTagInPost(Tag tag, int postId, User user) {
        checkAccessPermissions(tag.getId(), user, CREATE_TAG_MESSAGE_ERROR);
        this.tagRepository.createTagInPost(tag, postId, user);
    }

    @Override
    public void updateTagInPost(Tag tag, User user) {
        checkAccessPermissions(tag.getId(), user, MODIFY_TAG_ERROR_MESSAGE);
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
        checkAccessPermissions(tagId, user, DELETE_TAG_MESSAGE_ERROR);
        this.tagRepository.deleteTagInPost(tagId);
    }
}