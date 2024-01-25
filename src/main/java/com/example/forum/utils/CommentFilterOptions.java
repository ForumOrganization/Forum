package com.example.forum.utils;

import com.example.forum.models.Post;
import com.example.forum.models.User;

import java.util.Optional;

public class CommentFilterOptions {
    private Optional<User> user;
    private Optional<Post> post;
    private Optional<String> content;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public CommentFilterOptions(
            User user,
            Post post,
            String content,
            String sortBy,
            String sortOrder) {
        this.user = Optional.ofNullable(user);
        this.post = Optional.ofNullable(post);
        this.content = Optional.ofNullable(content);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }


    public Optional<User> getUser() {
        return user;
    }

    public void setUser(Optional<User> user) {
        this.user = user;
    }

    public Optional<Post> getPost() {
        return post;
    }

    public void setPost(Optional<Post> post) {
        this.post = post;
    }

    public Optional<String> getContent() {
        return content;
    }

    public void setContent(Optional<String> content) {
        this.content = content;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }
}
