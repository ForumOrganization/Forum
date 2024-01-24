package com.example.forum.utils;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;

import java.util.Optional;

public class UserFilterOptions {
    private Optional<String> name;
    private Optional<Post> posts;
    private Optional<Tag> tags;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public UserFilterOptions(
            String name,
            Post post,
            Tag tag,
            String sortBy,
            String sortOrder) {
        this.name = Optional.ofNullable(name);
        this.posts = Optional.ofNullable(post);
        this.tags = Optional.ofNullable(tag);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<Post> getPosts() {
        return posts;
    }

    public Optional<Tag> getTags() {
        return tags;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

}
