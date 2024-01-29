package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.utils.UserFilterOptions;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAll(UserFilterOptions userFilterOptions);

    User getById(int id, User user);
    void addPhoneNumberToAdmin(User admin, String phoneNumber);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    User getUserByComment(int commentId);

    List<Post> getPosts(int id);

    void registerUser(User user);

    void updateUser(User targetUser, User executingUser);

    void deleteUser(int deleteUserId, User executingUser);

    void updateToAdmin(User userAdmin, User updateToAdmin);

    void blockUser(User admin, User blockUser);

    void unBlockUser(User admin, User unBlockUser);

    void updatePhoneNumber(String phoneNumber);


    void deletePhoneNumber(int userId);

}