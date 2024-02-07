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

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<String> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Optional<String> createdBy) {
        this.createdBy = createdBy;
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