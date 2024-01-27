package com.example.forum.repositories.contracts;

import com.example.forum.models.PhoneNumber;
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

    List<Post> getPosts(int userId);

    void create(User user);

    void update(User targetUser);

    void updateToAdmin(User targetUser);

    void addPhoneNumberToAdmin(User admin, PhoneNumber phoneNumber);
}