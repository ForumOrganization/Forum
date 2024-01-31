package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.utils.UserFilterOptions;

import java.util.List;

public interface UserService {

    List<User> getAll(UserFilterOptions userFilterOptions);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getByFirstName(String firstName);

    User getUserByComment(int commentId);

    List<Post> getPosts(int id);

    void registerUser(User user);

    void updateUser(User targetUser, User executingUser);

    void deleteUser(int deleteUserId, User executingUser);

    void updateToAdmin(User userAdmin, User updateToAdmin);

    void blockUser(User admin, User blockUser);

    void unBlockUser(User admin, User unBlockUser);

    void addPhoneNumberToAdmin(User admin, User userPhoneNumberToBeUpdate);

    void deletePhoneNumber(int userId, User user);
}