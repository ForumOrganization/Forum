package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;

import java.util.List;

public interface UserService {

    //TODO Yoana
    List<UserDto> getAll();

    //TODO Veronika
    User getById(int id,User user);

    //TODO Siyana
    User getByUsername(String username);

    //TODO Yoana
    User getByEmail(String email);

    //TODO Veronika
    User getByFirstName(String firstName);

    //TODO Siyana
    List<Post> getPosts(int id, User user);

    //TODO Yoana
    void editPosts(int id, User user);

    //TODO Veronika
    void update(User user);

    //TODO Siyana
    User updateToAdmin(User userAdmin, User updateToAdmin);

    //TODO Yoana
    User blockUser(User admin, User blockUser);

    //TODO Veronika
    User unBlockUser(User admin, User unBlockUser);

    //TODO Siyana
    void addPhoneNumberToAdmin(User admin, String phoneNumber);
}