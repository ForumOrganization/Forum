package com.example.forum.models;

import java.util.Date;
import java.util.Optional;

public class FilterOptions {

    private Optional<String> title;
    private Optional<User> createdBy;
    private Optional<Date> creationTime;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions(
            String title,
            User createdBy,
            Date creationTime,
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

    public Optional<User> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Optional<User> createdBy) {
        this.createdBy = createdBy;
    }

    public Optional<Date> getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Optional<Date> creationTime) {
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