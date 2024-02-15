package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.utils.UserFilterOptions;

import java.util.List;

public interface UserService {

    List<User> getAll(UserFilterOptions userFilterOptions);

    long getAllNumber();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getByFirstName(String firstName);

    User getUserByComment(int commentId);

    List<Post> getPosts(int id);

    void registerUser(User user);

    void updateUser(User executingUser, User targetUser);

    void deleteUser(int deleteUserId, User executingUser);

    void saveProfilePictureUrl(String username, String profilePictureUrl);

    String getProfilePictureUrl(String username);

    void updateToAdmin(User updateToAdmin, User userAdmin);
    void updateToUser(User targetUser, User executingUser);

    void blockUser(User admin, User blockUser);

    void unBlockUser(User admin, User unBlockUser);

    void addPhoneNumberToAdmin(User admin, User userPhoneNumberToBeUpdate);

    void deletePhoneNumber(int userId, User user);
}