package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.UserFilterOptions;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;
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
    public User getById(int id,User user) {
        return this.userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return this.userRepository.getByUsername(username);
    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.getRole().equals("ADMIN") && executingUser.getId() != targetUserId) {
            throw new AuthorizationException(MODIFY_BEER_MESSAGE_ERROR);
        }
    }
}