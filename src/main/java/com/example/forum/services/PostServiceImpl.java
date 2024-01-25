package com.example.forum.services;

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

import java.util.List;

import static com.example.forum.utils.CheckPermission.checkAccessPermissions;
import static com.example.forum.utils.Messages.MODIFY_POST_ERROR_MESSAGE;
import static com.example.forum.utils.Messages.USER_HAS_BEEN_BLOCKED_OR_DELETED;

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
    public Post getById(int id) {
        return this.postRepository.getById(id);
    }

    @Override
    public Post getByTitle(String title) {
        return this.postRepository.getByTitle(title);
    }

    @Override
    public void create(Post post, User user) {

        if(user.getStatus()== Status.BLOCKED || user.isDeleted()){
            throw new UnauthorizedOperationException(USER_HAS_BEEN_BLOCKED_OR_DELETED);
        }
        boolean duplicateExists = true;

        try {
            this.postRepository.getById(post.getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Post", "id", String.valueOf(post.getId()));
        }

        post.setCreatedBy(user);
        this.postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        checkAccessPermissions(post.getId(), user, MODIFY_POST_ERROR_MESSAGE);
        boolean duplicateExists = true;

        try {
            Post existingPost = this.postRepository.getByTitle(post.getTitle());

            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Post", "title", post.getTitle());
        }

        this.postRepository.update(post);
    }

    @Override
    public void delete(int id, User user) {
        checkAccessPermissions(id, user, MODIFY_POST_ERROR_MESSAGE);
        this.postRepository.delete(id);
    }


}