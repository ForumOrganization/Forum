package com.example.forum.utils;

import com.example.forum.models.Post;

import java.time.LocalDate;
import java.util.Optional;

public class CommentFilterOptions {

    // private Optional<User> user;
    private Optional<Post> post;
    private Optional<String> content;
    private Optional<LocalDate> creationTime;

    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public CommentFilterOptions(
            Post post,
            String content,
            LocalDate creationTime,
            String sortBy,
            String sortOrder) {
        this.post = Optional.ofNullable(post);
        this.content = Optional.ofNullable(content);
        this.creationTime = Optional.ofNullable(creationTime);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
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

    public Optional<LocalDate> getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Optional<LocalDate> creationTime) {
        this.creationTime = creationTime;
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