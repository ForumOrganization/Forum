package com.example.forum.models.dtos;

import com.example.forum.helpers.PhoneNumberMapper;
import com.example.forum.models.Post;
import com.example.forum.models.enums.Role;

import java.util.Objects;
import java.util.Set;

public class UserResponseDto {

    private int id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private Role role = Role.USER;

    private PhoneNumberDto phoneNumber;

    private Set<Post> posts;

    // private Set<Comment> comments;

    public UserResponseDto() {
    }

    public UserResponseDto(int id, String username, String firstName, String lastName, String email, Role role, PhoneNumberDto phoneNumber, Set<Post> posts) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public PhoneNumberDto getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumberDto phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
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