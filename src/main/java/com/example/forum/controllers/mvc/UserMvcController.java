package com.example.forum.controllers.mvc;

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
import com.example.forum.models.dtos.UserFilterDto;
import com.example.forum.models.enums.Role;
import com.example.forum.services.contracts.UserService;
import com.example.forum.utils.PostFilterOptions;
import com.example.forum.utils.UserFilterOptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllUsers(@ModelAttribute("userFilterOptions") UserFilterDto filterDto, Model model) {
        UserFilterOptions userFilterOptions = new UserFilterOptions(
                filterDto.getUsername(),
                filterDto.getFirstName(),
                filterDto.getLastName(),
                filterDto.getEmail(),
                filterDto.getRole(),
                filterDto.getStatus(),
                filterDto.getSortBy(),
                filterDto.getSortOrder());
        List<User> users = userService.getAll(userFilterOptions);
        model.addAttribute("filterOptions", filterDto);
        model.addAttribute("users", users);
                userService.getAll(userFilterOptions);
        return "AdminPortalView";
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model,HttpSession session) {

            try {
               authenticationHelper.tryGetCurrentUser(session);
            } catch (AuthorizationException e) {
                return "redirect:/auth/login";
            }

        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
            model.addAttribute("isAuthenticated", true);
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{userId}/posts")
    public String showAllUserPosts(@PathVariable int userId, Model model, HttpSession session) {
        try {
            User user;
            try {
                user = authenticationHelper.tryGetCurrentUser(session);
            } catch (AuthorizationException e) {
                return "redirect:/auth/login";
            }

            User targetUser = userService.getById(userId);
            Set<Post> posts = targetUser.getPosts();
            model.addAttribute("posts", posts);

            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/new")
    public String showNewUserPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", new UserDto());
        return "UserCreateView";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }

        try {
            User userToCreate = userMapper.fromDtoRegister(userDto);
            userService.registerUser(userToCreate);
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "duplicate_user", e.getMessage());
            return "UserCreateView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User user = userService.getById(id);
            UserDto userDto = userMapper.userToDto(user);
            model.addAttribute("userId", id);
            model.addAttribute("user", userDto);
            return "UserUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id,
                             @Valid @ModelAttribute("user") UserDto dto,
                             @Valid PhoneNumberDto phoneNumberDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "UserUpdateView";
        }

        try {
            User userToUpdate = userMapper.fromDtoUpdate(id, dto);

            if (userToUpdate.getRole() == Role.ADMIN && phoneNumberDto != null) {
                User userPhoneNumberToBeUpdate = userMapper.fromDtoUpdatePhoneNumber(id, phoneNumberDto);
                userService.addPhoneNumberToAdmin(user, userPhoneNumberToBeUpdate);
            }

            userService.updateUser(user, userToUpdate);
            return "redirect:/user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
//        catch (DuplicateEntityException e) {
//            bindingResult.rejectValue("username", "duplicate_user", e.getMessage());
//            return "UserUpdateView";
//        }
        catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityAlreadyDeleteException e) {
            model.addAttribute("statusCode", HttpStatus.GONE.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            userService.deleteUser(id, user);
//            session.invalidate();
            session.isNew();
            session.invalidate();
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

//    @GetMapping("/{id}/update-to-admin")
//    public String updateToAdminForm(@PathVariable int id, Model model) {
//        model.addAttribute("userId", id);
//        return "UpdateToAdminView";
//    }

    @PostMapping("/{id}/update-to-admin")
    public String updateToAdmin(@PathVariable int id,
                                Model model,
                                HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User userToUpdate = userService.getById(id);
            userService.updateToAdmin(userToUpdate, user);
            return "redirect:/admin";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }  catch (DuplicateEntityException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    @PostMapping("/{id}/update-to-user")
    public String updateToUser(@PathVariable int id,
                                Model model,
                                HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User userToUpdate = userService.getById(id);
            userService.updateToUser(userToUpdate, user);
            return "redirect:/admin";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }  catch (DuplicateEntityException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/block")
    public String blockUser(@PathVariable int id, HttpSession session, Model model) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            User userToBeBlocked = userService.getById(id);
            userService.blockUser(currentUser, userToBeBlocked);
            return "redirect:/admin";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/unblock")
    public String unBlockUser(@PathVariable int id, HttpSession session, Model model) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            User userToBeUnblocked = userService.getById(id);
            userService.unBlockUser(currentUser, userToBeUnblocked);
            return "redirect:/admin";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/upload-profile-picture")
    public String uploadProfilePicture(@PathVariable int id, @RequestParam("file") MultipartFile file,
                                       HttpSession session, Model model) {
        try {
            String fileName = "/mages/" + file.getOriginalFilename();
            String uploadDir = "static/images/";

            String relativeUrl = "/images/" + fileName;
            userService.saveProfilePictureUrl(
                    authenticationHelper.tryGetCurrentUser(session).getUsername(), relativeUrl);
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            return "AccessDeniedView";
        }

        return "redirect:/users/username/" + authenticationHelper
                .tryGetCurrentUser(session)
                .getUsername();
    }
}