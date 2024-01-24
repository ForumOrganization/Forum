package com.example.forum.repositories.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;

import java.util.List;

public interface UserRepository {

    List<UserDto> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    List<Post> getPosts(int id, User user);

    void editPosts(int id, User user);

    void update(User user);

    User updateToAdmin(User userAdmin, User updateToAdmin);

    User blockUser(User admin, User blockUser);

    User unBlockUser(User admin, User unBlockUser);

    void addPhoneNumberToAdmin(User admin, String phoneNumber);
}