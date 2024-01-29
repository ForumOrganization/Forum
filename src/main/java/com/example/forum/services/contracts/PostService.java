package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.utils.PostFilterOptions;

import java.util.List;

public interface PostService {

    //TODO Veronika
    List<Post> getAll(PostFilterOptions postFilterOptions);

    List<Post> getTopCommentedPosts(PostFilterOptions postFilterOptions, int limit);

    List<Post> getMostRecentPosts(PostFilterOptions postFilterOptions, int limit);

    Post getById(int id);

    Post getByTitle(String title);

    Post getByComment(int commentId);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id, User user);
}