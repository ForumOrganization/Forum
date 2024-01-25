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

@Service
public class UserServiceImpl implements UserService {

    public static final String MODIFY_BEER_MESSAGE_ERROR = "Only admins or user created the beer can modify beer.";

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
        checkAccessPermissions(id, user);
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
    public void editPosts(int id, User user) {
        this.userRepository.editPosts(id, user);
    }

    @Override
    public void update(User targetUser, User executingUser) {
        checkAccessPermissionsUser(targetUser.getId(), executingUser);
        userRepository.update(targetUser);
    }

    @Override
    public User updateToAdmin(User userAdmin, User updateToAdmin) {
        return null;
    }

    @Override
    public User blockUser(User admin, User blockUser) {
        return this.userRepository.blockUser(admin, blockUser);
    }

    @Override
    public void unBlockUser(User admin, User unBlockUser) {
        checkAccessPermissionsAdmin(admin);
        unBlockUser.setStatus(Status.ACTIVE);
        userRepository.update(unBlockUser);
    }

    @Override
    public void addPhoneNumberToAdmin(User admin, String phoneNumber) {

    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.getRole().name().equals("ADMIN") && executingUser.getId() != targetUserId) {
            throw new AuthorizationException(MODIFY_BEER_MESSAGE_ERROR);
        }
    }

    private static void checkAccessPermissionsAdmin(User executingUser) {
        if (!executingUser.getRole().name().equals("ADMIN")) {
            throw new AuthorizationException(MODIFY_BEER_MESSAGE_ERROR);
        }
    }
    private static void checkAccessPermissionsUser(int targetUserId, User executingUser) {
        if (executingUser.getId() != targetUserId) {
            throw new AuthorizationException(MODIFY_BEER_MESSAGE_ERROR);
        }
    }
}