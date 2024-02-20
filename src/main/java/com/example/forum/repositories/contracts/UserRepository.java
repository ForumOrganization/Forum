package com.example.forum.repositories.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.utils.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    List<User> getAll(UserFilterOptions userFilterOptions);

    long getAllNumber();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getByFirstName(String firstName);

    User getUserByComment(int commentId);

    List<Post> getPosts(int userId);

    void registerUser(User user);

    void reactivated(User targetUser);

    void updateUser(User targetUser);

    void deleteUser(int targetUserId);

    boolean isDataBaseEmpty();

    boolean existsByPhoneNumber(User userPhoneNumberToBeUpdate);
}