package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.utils.PostFilterOptions;

import java.util.List;

public interface PostService {

    List<Post> getAll(PostFilterOptions postFilterOptions);

    public List<Post> getTopCommentedPosts();

    public List<Post> getMostRecentPosts();

    Post getById(int id);

    Post getByTitle(String title);

    Post getByComment(int commentId);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id, User user);
}