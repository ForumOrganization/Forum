package com.example.forum.utils;

import com.example.forum.models.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> title;
    private Optional<String> createdBy;
    private Optional<LocalDate> creationTime;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public PostFilterOptions() {
        this(null,null,null,null,null);
    }

    public PostFilterOptions(
            String title,
            String createdBy,
            LocalDate creationTime,
            String sortBy,
            String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.createdBy = Optional.ofNullable(createdBy);
        this.creationTime = Optional.ofNullable(creationTime);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getCreatedBy() {
        return createdBy;
    }

    public Optional<LocalDate> getCreationTime() {
        return creationTime;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}