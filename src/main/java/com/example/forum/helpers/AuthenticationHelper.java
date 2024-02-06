package com.example.forum.helpers;

import com.example.forum.exceptions.AuthenticationFailureException;
import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.User;
import com.example.forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);

            User user = this.userService.getByUsername(username);

            if (!user.getPassword().equals(password)) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;

        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getPassword(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");

        if (firstSpaceIndex == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpaceIndex + 1);
    }

    private String getUsername(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");

        if (firstSpaceIndex == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpaceIndex);
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByUsername(username);

            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    public User tryGetUser(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");

        if (currentUser == null) {
            throw new AuthenticationFailureException("No user logged in.");
        }

        return userService.getByUsername(currentUser);
    }
}