package com.example.forum.services.contracts;

import com.example.forum.models.Comment;
import com.example.forum.utils.PostFilterOptions;
import com.example.forum.models.Post;
import com.example.forum.models.User;

import java.util.List;
import java.util.Set;

public interface PostService {

    //TODO Veronika
    List<Post> getAll(PostFilterOptions postFilterOptions);

    Post getById(int id);

    //TODO Yoana
    Post getByTitle(String title);

    //TODO Veronika
    void create(Post post, User user);

    void update(Post post, User user);

    //TODO Yoana
    void delete(int id, User user);
}