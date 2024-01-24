package com.example.forum.controllers;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.UserMapper;
import com.example.forum.models.*;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.models.enums.Role;
import com.example.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private static final String UNAUTHORIZED_USER_ERROR_MESSAGE = "You are not authorized to browse user information.";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }
    @GetMapping
    public List<UserDto> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.getRole().equals(Role.USER)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
            }
            return userService.getAll();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }


    @GetMapping("/{id}")
    public UserResponseDto getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.getById(id, user);
            return new UserResponseDto(user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getPosts(),
                    user.getComments());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }
    @GetMapping("/search")
    public User getByName(@RequestParam String name) {
        try {
            return userService.getByUsername(name);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/posts")
    public List<Post> getPosts(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
          // userService.getPosts(id, user);
            return new ArrayList<>(user.getPosts());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }
}