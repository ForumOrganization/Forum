package com.example.forum.services.contracts;

import com.example.forum.models.Comment;
import com.example.forum.models.PhoneNumber;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.utils.UserFilterOptions;

import java.util.List;

public interface UserService {

    //TODO Yoana
    List<UserResponseDto> getAll(UserFilterOptions userFilterOptions);

    //TODO Veronika - Done
    User getById(int id, User user);

    //TODO Siyana - Done
    User getByUsername(String username);

    //TODO Yoana
    User getByEmail(String email);

    //TODO Veronika - Done
    User getByFirstName(String firstName);

    //TODO Veronika - Done
    User getUserByComment(int commentId);

    //TODO Siyana - Done
    List<Post> getPosts(int id);

    //TODO Veronika
    void registerUser(User user);

    //TODO Veronika - Done
    void updateUser(User targetUser, User executingUser);

    void deleteUser(int deleteUserId,User executingUser);

    //TODO Siyana - Done
    void updateToAdmin(User userAdmin, User updateToAdmin);

    //TODO Yoana
    void blockUser(User admin, User blockUser);

    //TODO Veronika -Done
    void unBlockUser(User admin, User unBlockUser);
}