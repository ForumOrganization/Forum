package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.UserRepository;
import com.example.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forum.utils.CheckPermission.*;
import static com.example.forum.utils.Messages.*;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAll() {
        return this.userRepository.getAll();
    }

    @Override
    public User getById(int id, User user) {
        return this.userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return this.userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return this.userRepository.getByEmail(email);
    }

    @Override
    public User getByFirstName(String firstName) {
        return this.userRepository.getByUsername(firstName);
    }

    @Override
    public List<Post> getPosts(int id, User user) {
        return null;
    }

    @Override
    public void create(User user) {

    }

    @Override
    public void update(User targetUser, User executingUser) {
        checkAccessPermissionsUser(targetUser.getId(), executingUser, MODIFY_USER_MESSAGE_ERROR);
        userRepository.update(targetUser);
    }

    @Override
    public User updateToAdmin(User userAdmin, User updateToAdmin) {
        return null;
    }

    @Override
    public void blockUser(User admin, User blockUser) {
        checkAccessPermissionsAdmin(admin, MODIFY_ADMIN_MESSAGE_ERROR);
        blockUser.setStatus(Status.BLOCKED);
        userRepository.update(blockUser);
    }

    @Override
    public void unBlockUser(User admin, User unBlockUser) {
        checkAccessPermissionsAdmin(admin, MODIFY_ADMIN_MESSAGE_ERROR);
        unBlockUser.setStatus(Status.ACTIVE);
        userRepository.update(unBlockUser);
    }

    @Override
    public void addPhoneNumberToAdmin(User admin, String phoneNumber) {

    }

}