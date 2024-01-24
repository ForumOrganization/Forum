package com.example.forum.repositories.contracts;

import com.example.forum.utils.PostFilterOptions;
import com.example.forum.models.Post;
import com.example.forum.models.User;

import java.util.List;
import java.util.Set;

public interface PostRepository {

    List<Post> getAll(PostFilterOptions postFilterOptions);

    Post getById(int id);

    Post getByTitle(String title);

    void create(Post post);

    void update(Post post);

    void delete(int id);
}