package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.models.enums.Role;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.UserRepository;
import com.example.forum.services.contracts.UserService;
import com.example.forum.utils.UserFilterOptions;
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
    public List<UserResponseDto> getAll(UserFilterOptions userFilterOptions) {
        return this.userRepository.getAll(userFilterOptions);
    }

    @Override
    public User getById(int id, User user) {
        return this.userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.getByUsername(username);
        checkAccessPermissionsAdmin(user, String.format(SEARCH_ADMIN_MESSAGE_ERROR, "username"));
        return this.userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.getByEmail(email);
        checkAccessPermissionsAdmin(user, String.format(SEARCH_ADMIN_MESSAGE_ERROR, "email"));
        return this.userRepository.getByEmail(email);
    }

    @Override
    public User getByFirstName(String firstName) {
        User user = userRepository.getByFirstName(firstName);
        checkAccessPermissionsAdmin(user, String.format(SEARCH_ADMIN_MESSAGE_ERROR, "first name"));
        return this.userRepository.getByUsername(firstName);
    }

    @Override
    public User getUserByComment(int commentId) {
        return this.userRepository.getUserByComment(commentId);
    }

    @Override
    public List<Post> getPosts(int id) {
        return this.userRepository.getPosts(id);
    }

    @Override
    public void registerUser(User user) {
        if (user.getUsername() == null) {
            throw new EntityNotFoundException("User", "username", user.getUsername());
        }

        User existingUser = userRepository.getByUsername(user.getUsername());

        if (existingUser.getUsername().equals(user.getUsername())
                && existingUser.getFirstName().equals(user.getFirstName())
                && existingUser.getLastName().equals(user.getLastName())
                && existingUser.getEmail().equals(user.getEmail())
                && existingUser.isDeleted()) {
            userRepository.reactivated(existingUser);
        } else {

            if (userRepository.getByUsername(user.getUsername()) != null) {
                throw new DuplicateEntityException("User", "username", user.getUsername());
            }

            if (userRepository.getByEmail(user.getEmail()) != null) {
                throw new DuplicateEntityException("User", "email", user.getEmail());
            }

            userRepository.registerUser(user);
        }
    }

    @Override
    public void updateUser(User targetUser, User executingUser) {
        checkAccessPermissionsUser(targetUser.getId(), executingUser, MODIFY_USER_MESSAGE_ERROR);

        if (!targetUser.getUsername().equals(executingUser.getUsername())) {
            throw new EntityNotFoundException("User", "username", targetUser.getUsername());
        }

        if (userRepository.getByEmail(targetUser.getEmail()) != null) {
            throw new DuplicateEntityException("User", "email", targetUser.getEmail());
        }

        userRepository.updateUser(targetUser);
    }

    @Override
    public void deleteUser(int deleteUser, User executingUser) {
        checkAccessPermissions(deleteUser, executingUser, DELETE_USER_MESSAGE_ERROR);

        User userToDelete = getById(deleteUser, executingUser);

        if (userToDelete.isDeleted()) {
            throw new DuplicateEntityException("User", "id", String.valueOf(deleteUser));
        }

        userRepository.deleteUser(deleteUser);
    }

    @Override
    public void updateToAdmin(User targetUser, User executingUser) {
        checkAccessPermissionsAdmin(executingUser, UPDATE_PHONENUMBER_ERROR_MESSAGE);
        checkAccessPermissionsUser(targetUser.getId(), executingUser, UPDATE_TO_ADMIN_ERROR_MESSAGE);

        targetUser.setRole(Role.ADMIN);
        userRepository.updateToAdmin(targetUser);
    }

    @Override
    public void blockUser(User admin, User blockUser) {
        checkAccessPermissionsAdmin(admin, MODIFY_ADMIN_MESSAGE_ERROR);
        blockUser.setStatus(Status.BLOCKED);
        userRepository.updateUser(blockUser);
    }

    @Override
    public void unBlockUser(User admin, User unBlockUser) {
        checkAccessPermissionsAdmin(admin, MODIFY_ADMIN_MESSAGE_ERROR);
        unBlockUser.setStatus(Status.ACTIVE);
        userRepository.updateUser(unBlockUser);
    }

    @Override
    public void updatePhoneNumber(String phoneNumber) {

    }

    @Override
    public void addPhoneNumberToAdmin(User admin, String phoneNumber) {
        checkAccessPermissionsAdmin(admin, UPDATE_PHONENUMBER_ERROR_MESSAGE);
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            if (phoneNumber.equals(admin.getPhoneNumber())) {
                throw new DuplicateEntityException("Admin", "phone number", phoneNumber);
            }
            if (admin.getPhoneNumber() == null || admin.getPhoneNumber().isEmpty()) {
                admin.setPhoneNumber(phoneNumber);
                userRepository.addPhoneNumberToAdmin(admin);
            } else {
                admin.setPhoneNumber(phoneNumber);
                userRepository.updatePhoneNumber(admin);
            }
        } else {
            throw new EntityNotFoundException("Admin", "phone number");
        }
    }

    @Override
    public void deletePhoneNumber(int userId, User user) {
        User userToDelete = userRepository.getById(userId);
        checkAccessPermissionsAdmin(userToDelete, DELETE_PHONENUMBER_MESSAGE_ERROR);
        checkAccessPermissionsUser(userId, user, DELETE_PHONENUMBER_MESSAGE_ERROR);
        userRepository.deletePhoneNumber(userId);
    }
}