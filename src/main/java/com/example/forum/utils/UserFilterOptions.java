package com.example.forum.utils;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.enums.Status;

import java.util.Optional;

public class UserFilterOptions {
    private Optional<String> username;
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> email;
    private Optional<Status> role;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public UserFilterOptions(
            String username,
            String firstName,
            String lastName,
            String email,
            Status role,
            String sortBy,
            String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.email = Optional.ofNullable(email);
        this.role = Optional.ofNullable(role);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<Status> getRole() {
        return role;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}