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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(tags ={"Get all users"},
            summary = "This method retrieve information about all users.",
            description = "This method search for all users in the data base. When a person is authorized and there are registered users, a list with all users will be presented.",
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "User(s) was/were found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to access the list of users."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User(s) is/were not found.")})
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
            User user = authenticationHelper.tryGetUser(headers);
            UserFilterOptions userFilterOptions =
                    new UserFilterOptions(
                            username, firstName, lastName, email, role, status, sortBy, sortOrder);

            List<User> users = userService.getAll(user, userFilterOptions);

            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping("/{id}")
    @Operation(tags ={"Get a user"},
            operationId = "Id to be searched for",
            summary = "This method search for a user when id is given.",
            description = "This method search for a user. A valid id must be given as an input.",
            parameters = {@Parameter( name = "id", description = "path variable", example = "5")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to access this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found.")})
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
    @Operation(tags ={"Search for a user"},
            summary = "This method search for a user when user name is given.",
            description = "This method search for a user. A valid user name must be given as an input.",
            parameters = {@Parameter( name = "username", description = "Username", example = "yoana")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to search for this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this title was not found.")})
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
    @Operation(tags ={"Search for a user"},
            summary = "This method search for a user when user's email is given.",
            description = "This method search for a user. A valid user email must be given as an input.",
            parameters = {@Parameter( name = "email", description = "User's email address", example = "yoana@abv.bg")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to search for this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this title was not found.")})
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
    @Operation(tags ={"Search for a user"},
            summary = "This method search for a user when user's first name is given.",
            description = "This method search for a user. User's first name must be given as an input.",
            parameters = {@Parameter( name = "firstName", description = "Fisrt name of the user", example = "Yoana")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to search for this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this first name was not found.")})
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
    @Operation(tags ={"Search for a user"},
            summary = "This method search for a user when a comment created by the user is given.",
            description = "This method search for a user. A comment created by this user must be given as an input.",
            parameters = {@Parameter( name = "id", description = "Id of the comment", example = "3")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to search for this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "No such comment was not found.")})
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
    @Operation(tags ={"Search for a user's post(s)"},
            summary = "This method search for a user's posts when user's id is given as an input.",
            description = "This method search for a user's posts. List of posts created by this user must be given as an input when a valid user id is given.",
            parameters = {@Parameter( name = "id", description = "user id", example = "5")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user's post(s) was/were found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to search for this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "Posts related to this user was not found.")})
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
    @Operation(tags ={"Register user"},
            summary = "Using this method, a new user can be registered.",
            description = "This method do registering of a new user. When valid parameters are given as an input, a new user is being created.",
//
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts user dto object as a parameter.",
                    content = @Content(schema = @Schema(implementation = User.class))),
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been created successfully"),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = User.class)), description = "There is a conflict."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "Not found.")})
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
    @Operation(tags ={"Update a user"},
            summary = "Using this method, a user is being updated.",
            description = "This method updates the fields of a user. When valid id and parameters are given as an input, the user is updated.",
            parameters = {@Parameter( name = "id", description = "user's id", example = "1")},
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts user dto object as a parameter.",
            content = @Content(schema = @Schema(implementation = User.class))),
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been updated successfully"),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = User.class)), description = "There is a conflict."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to update this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found")})
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
    @Operation(tags ={"Delete a user"},
            summary = "Using this method, a user is being deleted.",
            description = "This method deletes a user. When valid id is given as an input, the user is deleted.",
            parameters = {@Parameter( name = "id", description = "user's id", example = "1")},
            responses ={@ApiResponse(responseCode = "410", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been deleted successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to delete this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found.")})
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
    @Operation(tags ={"Update a user to admin"},
            summary = "Using this method, a user is being updated to be an admin.",
            description = "This method change the role of a user to be an admin. When valid id is given as an input, the user is being updated.",
            parameters = {@Parameter( name = "id", description = "user's id", example = "1")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been updated successfully"),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = User.class)), description = "There is a conflict."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to update this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found.")})
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
    @Operation(tags ={"Block a user"},
            summary = "Using this method, a user can be blocked.",
            description = "When a valid id is given and there is the proper authorization, a user'status can be changed to blocked.",
            parameters = {@Parameter( name = "id", description = "user's id", example = "1")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been blocked successfully"),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = User.class)), description = "There is a conflict."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to block this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found.")})
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
    @Operation(tags ={"Unblock a user"},
            summary = "Using this method, a user can be unblocked.",
            description = "When a valid id is given and there is the proper authorization, a user'status can be changed from blocked to unblocked.",
            parameters = {@Parameter( name = "id", description = "user's id", example = "1")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user has been unblocked successfully"),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = User.class)), description = "There is a conflict."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to unblock this user."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found.")})
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
    @Operation(tags ={"Update user's phone number"},
            summary = "Using this method, a user's phone number can be updated.",
            description = "When a valid user id is given and there is the proper authorization, a user's phone number can be updated.",
            parameters = {@Parameter( name = "id", description = "user's id", example = "1")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts phone number dto object as a parameter.",
                    content = @Content(schema = @Schema(implementation = User.class))),
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user's phone number has been updated successfully"),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = User.class)), description = "There is a conflict."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to update this user's phone number."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found.")})
    public UserResponseDto updateUserPhoneNumber(@RequestHeader HttpHeaders headers,
                                                 @PathVariable int id,
                                                 @Valid @RequestBody PhoneNumberDto phoneNumberDto, UserDto dto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userPhoneNumberToBeUpdate = userMapper.fromDtoUpdatePhoneNumber(id, phoneNumberDto, dto);
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
    @Operation(tags ={"Delete user's phone number"},
            summary = "Using this method, a user's phone number can be deleted.",
            description = "When a valid user id is given and there is the proper authorization, a user's phone number can be deleted.",
            parameters = {@Parameter( name = "id", description = "user's id", example = "1")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)), description = "The user's phone number has been deleted successfully"),
                    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = User.class)), description = "There is a conflict."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = User.class)), description = "You are not allowed to delete this user's phone number."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = User.class)), description = "User with this id was not found.")})
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