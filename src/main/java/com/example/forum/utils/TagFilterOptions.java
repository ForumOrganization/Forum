package com.example.forum.utils;

import com.example.forum.models.Post;
import com.example.forum.models.enums.Status;

import java.util.Optional;

public class TagFilterOptions {
    private Optional<String> name;
    private Optional<Post> post;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public TagFilterOptions(
            String name,
            String sortBy,
            String sortOrder) {
        this.name = Optional.ofNullable(name);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}