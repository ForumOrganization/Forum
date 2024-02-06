package com.example.forum.repositories.contracts;

import com.example.forum.models.Post;
import com.example.forum.utils.PostFilterOptions;

import java.util.List;

public interface PostRepository {

    List<Post> getAll(PostFilterOptions postFilterOptions);

    List<Post> getTopCommentedPosts();

    List<Post> getMostRecentPosts();

    Post getById(int id);

    Post getByTitle(String title);

    Post getByComment(int commentId);

    void create(Post post);

    void update(Post post);

    void delete(int id);
}