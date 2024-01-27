package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;

import java.util.List;

public interface UserService {

    //TODO Yoana
    List<UserDto> getAll();

    //TODO Veronika - Done
    User getById(int id, User user);
    //TODO Siyana - Done
    User getByUsername(String username);

    //TODO Yoana
    User getByEmail(String email);

    //TODO Veronika - Done
    User getByFirstName(String firstName);
    //TODO Siyana - Done
    List<Post> getPosts(int id);

    //TODO Veronika
    void registerUser(User user);

    //TODO Veronika - Done
    void updateUser(User targetUser, User executingUser);
    //TODO Siyana - Done
    void updateToAdmin(User userAdmin, User updateToAdmin);

    //TODO Yoana
    void blockUser(User admin, User blockUser);

    //TODO Veronika -Done
    void unBlockUser(User admin, User unBlockUser);

    //TODO Siyana - Done
    void addPhoneNumberToAdmin(User admin, String phoneNumber);
}