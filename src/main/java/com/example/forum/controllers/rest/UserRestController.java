package com.example.forum.controllers.rest;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityAlreadyDeleteException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.UserMapper;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.PhoneNumberDto;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.models.enums.Role;
import com.example.forum.models.enums.Status;
import com.example.forum.services.contracts.UserService;
import com.example.forum.utils.UserFilterOptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.forum.utils.Messages.UNAUTHORIZED_USER_ERROR_MESSAGE;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService,
                              AuthenticationHelper authenticationHelper,
                              UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserResponseDto> getAll(@RequestHeader HttpHeaders headers,
                                        @RequestParam(required = false) String username,
                                        @RequestParam(required = false) String firstName,
                                        @RequestParam(required = false) String lastName,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) Role role,
                                        @RequestParam(required = false) Status status,
                                        @RequestParam(required = false) String sortBy,
                                        @RequestParam(required = false) String sortOrder) {
        try {
            authenticationHelper.tryGetUser(headers);
            UserFilterOptions userFilterOptions =
                    new UserFilterOptions(
                            username, firstName, lastName, email,role,status, sortBy, sortOrder);

            List<User> users = userService.getAll(userFilterOptions);

            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            User targetUser = userService.getById(id);
            return userMapper.toDto(targetUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping(value = "/search", params = {"username"})
    public UserResponseDto getByUsername(@RequestHeader HttpHeaders headers, @RequestParam String username) {
        try {
            authenticationHelper.tryGetUser(headers);
            User targetUser = userService.getByUsername(username);
            return userMapper.toDto(targetUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping(value = "/search", params = {"email"})
    public UserResponseDto getByEmail(@RequestHeader HttpHeaders headers, @RequestParam String email) {
        try {
            authenticationHelper.tryGetUser(headers);
            User targetUser = userService.getByEmail(email);
            return userMapper.toDto(targetUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping(value = "/search", params = {"firstName"})
    public List<UserResponseDto> getByFirstName(@RequestHeader HttpHeaders headers,
                                                @RequestParam String firstName) {
        try {
            authenticationHelper.tryGetUser(headers);
            List<User> users = userService.getByFirstName(firstName);
            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping("/comment/{id}")
    public UserResponseDto getUserByComment(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            User targetUser = userService.getUserByComment(id);
            return userMapper.toDto(targetUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping("/{id}/posts")
    public List<Post> getPosts(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            List<Post> userPosts = userService.getPosts(id);
            return new ArrayList<>(userPosts);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @PostMapping
    public UserResponseDto registerUser(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDtoRegister(userDto);
            userService.registerUser(user);
            return userMapper.toDtoRegisterAndUpdateUser(user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@RequestHeader HttpHeaders headers,
                                      @PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = userMapper.fromDtoUpdate(id, userDto);
            userService.updateUser(userToBeUpdated, user);
            return userMapper.toDtoRegisterAndUpdateUser(userToBeUpdated);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.deleteUser(id, user);
        } catch (EntityAlreadyDeleteException e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/admins")
    public UserResponseDto updateToAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToBeUpdate = userService.getById(id);
            userService.updateToAdmin(userToBeUpdate, user);
            return userMapper.toDto(userToBeUpdate);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/block")
    public UserResponseDto blockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToBeBlock = userService.getById(id);
            userService.blockUser(user, userToBeBlock);
            return userMapper.toDto(userToBeBlock);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/unblock")
    public UserResponseDto unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToBeUnblock = userService.getById(id);
            userService.unBlockUser(user, userToBeUnblock);
            return userMapper.toDto(userToBeUnblock);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/phone-number")
    public UserResponseDto updateUserPhoneNumber(@RequestHeader HttpHeaders headers,
                                                 @PathVariable int id,
                                                 @Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userPhoneNumberToBeUpdate = userMapper.fromDtoUpdatePhoneNumber(id, phoneNumberDto);
            userService.addPhoneNumberToAdmin(user, userPhoneNumberToBeUpdate);
            return userMapper.toDto(userPhoneNumberToBeUpdate);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/phone-number")
    public void deleteUserPhoneNumber(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.deletePhoneNumber(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityAlreadyDeleteException e) {
            throw new ResponseStatusException(HttpStatus.GONE, e.getMessage());
        }
    }
}