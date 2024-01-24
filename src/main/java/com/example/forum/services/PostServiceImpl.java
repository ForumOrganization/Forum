package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.utils.PostFilterOptions;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private static final String MODIFY_BEER_ERROR_MESSAGE = "Only admin or post creator can modify post.";

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
    public Set<Post> getByUser(User user) {
        return this.postRepository.getByUser(user);
    }

    @Override
    public void create(Post post, User user) {
        boolean duplicateExists = true;

        try {
            this.postRepository.getByTitle(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Post", "title", post.getTitle());
        }

        post.setCreatedBy(user);
        this.postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        checkModifyPermission(post.getId(), user);

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
        checkModifyPermission(id, user);

        this.postRepository.delete(id);
    }

    private void checkModifyPermission(int postId, User user) {
        Post post = this.postRepository.getById(postId);

        if (!(user.getRole().name().equals("ADMIN") || post.getCreatedBy().equals(user))) {
            throw new AuthorizationException(MODIFY_BEER_ERROR_MESSAGE);
        }
    }
}