package controllers;

import exceptions.AuthorizationException;
import exceptions.EntityNotFoundExceptions;
import helpers.AuthenticationHelper;
import helpers.UserMapper;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import services.UserService;

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
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
            }

            return userService.getAll();

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return userService.getById(id);
        } catch (EntityNotFoundExceptions e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId) {
            throw new AuthorizationException(UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }
}