package com.example.forum.models.dtos;


import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.Set;

public class UserResponseDto {

    @JsonIgnore
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role = Role.USER;
    private String phoneNumber;
    private Set<Post> posts;
    private Set<Comment> comments;

    public UserResponseDto() {
    }

    public UserResponseDto(int id, String firstName, String lastName,
                           String email, Role role, String phoneNumber,
                           Set<Post> posts, Set<Comment> comments) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.posts = posts;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDto userResponseDto = (UserResponseDto) o;
        return id == userResponseDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}