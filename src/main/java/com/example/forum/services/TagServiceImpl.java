package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.repositories.contracts.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Tag> getAllTags(TagFilterOptions tagFilterOptions) {
        return tagRepository.getAllTags(tagFilterOptions);
    }

    @Override
    public Tag getTagById(int tagId) {
        return tagRepository.getTagById(tagId);
    }

    @Override
    public Tag getTagByName(String name) {
        boolean missingTag = false;
        Tag tag = new Tag();
        try {
          tag = tagRepository.getTagByName(name);
        } catch (EntityNotFoundException e) {
            missingTag = true;
        }

        if (missingTag) {
            throw new EntityNotFoundException("Tag", "name", name);

    } return tag;
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
        int authorId = author.getId();
        checkAccessPermissions(authorId, user, CREATE_TAG_MESSAGE_ERROR);
        this.tagRepository.createTagInPost(tag, postId, user);
    }

    @Override
    public void updateTagInPost(Tag tag, User user, int postId, int tagId) {
        User author = postRepository.getById(postId).getCreatedBy();
        int authorId = author.getId();
        checkAccessPermissions(authorId, user, MODIFY_TAG_ERROR_MESSAGE);

        boolean duplicateExists = true;
        boolean missingComment = false;
        boolean missingTag = false;

        try {
            tagRepository.getTagById(tagId);
        } catch (EntityNotFoundException e) {
            missingTag = true;
        }

        if (missingTag) {
            throw new EntityNotFoundException("Tag", "name", tag.getName());
        }

        try {
            postRepository.getById(postId);
        } catch (EntityNotFoundException e) {
            missingComment = true;
        }

        if (missingComment) {
            throw new EntityNotFoundException("Post", "id", String.valueOf(postId));
        }

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
    public void deleteTagInPost(Tag tag, User user, int postId, int tagId) {
        User author = postRepository.getById(postId).getCreatedBy();
        tagRepository.getTagById(tagId);
        int authorId = author.getId();
        checkAccessPermissions(authorId, user, DELETE_TAG_MESSAGE_ERROR);
        this.tagRepository.deleteTagInPost(postId, tagId);
    }
}