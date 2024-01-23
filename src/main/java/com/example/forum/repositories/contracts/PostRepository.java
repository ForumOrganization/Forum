package com.example.forum.repositories.contracts;

import com.example.forum.models.FilterOptions;
import com.example.forum.models.Post;
import com.example.forum.models.User;

import java.util.List;
import java.util.Set;

public interface PostRepository {

    List<Post> getAll(FilterOptions filterOptions);

    Post getById(int id);

    Post getByTitle(String title);

    Set<Post> getByUser(User user);

    void create(Post post);

    void update(Post post);

    void delete(int id);
}