package com.example.forum.repositories.contracts;

import com.example.forum.models.PhoneNumber;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.utils.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    List<UserResponseDto> getAll(UserFilterOptions userFilterOptions);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    User getUserByComment(int commentId);

    List<Post> getPosts(int userId);

    void registerUser(User user);

    void updateUser(User targetUser);
    void reactivated(User targetUser);
    void deleteUser(int targetUserId);

    void updateToAdmin(User targetUser);

    void addPhoneNumberToAdmin(User admin, PhoneNumber phoneNumber);
}