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

    List<Post> getTopCommentedPosts(PostFilterOptions postFilterOptions, int limit);

    List<Post> getMostRecentPosts(PostFilterOptions postFilterOptions, int limit);
    //TODO Siyana - Done
    Post getById(int id);

    //TODO Yoana
    Post getByTitle(String title);
    //TODO Veronika - Done
    Post getByComment(int commentId);

    //TODO Veronika - Done
    void create(Post post, User user);
    //TODO Siyana - Done
    void update(Post post, User user);

    //TODO Yoana
    void delete(int id, User user);
}