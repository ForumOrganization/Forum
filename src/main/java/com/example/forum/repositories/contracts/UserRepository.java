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

    void create(User user);

    void update(User targetUser);

    User updateToAdmin(User userAdmin, User updateToAdmin);

    void addPhoneNumberToAdmin(User admin, String phoneNumber);
}