package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.exceptions.UnauthorizedOperationException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.services.contracts.PostService;
import com.example.forum.utils.PostFilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.forum.utils.CheckPermission.checkAccessPermissions;
import static com.example.forum.utils.CheckPermission.checkAccessPermissionsUser;
import static com.example.forum.utils.Messages.*;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAll(PostFilterOptions postFilterOptions) {
        return this.postRepository.getAll(postFilterOptions);
    }

    @Override
    public List<Post> getTopCommentedPosts() {
      return this.postRepository.getTopCommentedPosts();

    }

    @Override
    public List<Post> getMostRecentPosts() {
        return this.postRepository.getMostRecentPosts();

    }

    @Override
    public Post getById(int id) {
        return this.postRepository.getById(id);
    }

    @Override
    public Post getByTitle(String title) {
        return this.postRepository.getByTitle(title);
    }

    @Override
    public Post getByComment(int commentId) {
        return this.postRepository.getByComment(commentId);
    }

    @Override
    public void create(Post post, User user) {
       checkBlockOrDeleteUser(user);

        post.setCreatedBy(user);
        this.postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        checkBlockOrDeleteUser(user);
        checkAccessPermissionsUser(post.getCreatedBy().getId(), user, MODIFY_USER_MESSAGE_ERROR);

        boolean duplicateExists = true;

        try {
            Post existingPost = this.postRepository.getById(post.getId());

            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Post", "id", String.valueOf(post.getId()));
        }

        this.postRepository.update(post);
    }

    @Override
    public void delete(int id, User user) {
        checkBlockOrDeleteUser(user);
        Post post = postRepository.getById(id);
        checkAccessPermissions(post.getCreatedBy().getId(), user, MODIFY_POST_ERROR_MESSAGE);
        this.postRepository.delete(id);
    }
    private static void checkBlockOrDeleteUser(User user) {
        if (user.getStatus() == Status.BLOCKED || user.isDeleted()) {
            throw new AuthorizationException(USER_HAS_BEEN_BLOCKED_OR_DELETED);
        }
    }
}