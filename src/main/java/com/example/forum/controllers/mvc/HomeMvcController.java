package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserFilterDto;
import com.example.forum.models.enums.Role;
import com.example.forum.services.contracts.PostService;
import com.example.forum.services.contracts.UserService;
import com.example.forum.utils.UserFilterOptions;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.example.forum.utils.Messages.UNAUTHORIZED_USER_ERROR_MESSAGE;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final PostService postService;

    public HomeMvcController(AuthenticationHelper authenticationHelper, UserService userService, PostService postService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.postService = postService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {

        long userNum = userService.getAllNumber();
        long postNum = postService.getAllNumber();
        model.addAttribute("userNumber", userNum);
        model.addAttribute("postNumber", postNum);

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            if (user != null) {
                model.addAttribute("currentUser", user);
            }

            return "HomeView";
        } catch (AuthorizationException e) {
            return "HomeView";
        }
    }

    @GetMapping("/about")
    public String showAboutPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);

            if (user != null) {
                model.addAttribute("currentUser", user);
            }

            return "AboutView";
        } catch (AuthorizationException e) {
            return "AboutView";
        }
    }

    @GetMapping("/admin")
    public String showAdminPortal(@ModelAttribute("userFilterOptions") UserFilterDto filterDto,
                                  HttpSession session, Model model) {
        UserFilterOptions userFilterOptions = new UserFilterOptions(
                filterDto.getUsername(),
                filterDto.getFirstName(),
                filterDto.getLastName(),
                filterDto.getEmail(),
                filterDto.getRole(),
                filterDto.getStatus(),
                filterDto.getSortBy(),
                filterDto.getSortOrder());
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            if (user.getRole() != Role.ADMIN) {
                throw new AuthorizationException(UNAUTHORIZED_USER_ERROR_MESSAGE);
            }

            List<User> users = userService.getAll(user, userFilterOptions);
            model.addAttribute("users", users);
            model.addAttribute("user", user);
            model.addAttribute("currentUser", user);
            model.addAttribute("filterOptions", filterDto);
            model.addAttribute("isAuthenticated", true);
            return "AdminPortalView";
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

    @GetMapping("/user")
    public String showUserProfile(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", user);
            model.addAttribute("isAuthenticated", true);
            return "UserProfileView";
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
    @GetMapping("/upload-profile-picture")
    public String getProfilePicture(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetCurrentUser(session);
        String profilePictureUrl = userService.getProfilePictureUrl(user.getUsername());
        model.addAttribute("profilePictureUrl", profilePictureUrl);
        model.addAttribute("currentUser", user);
        model.addAttribute("isAuthenticated", true);
        return "HomeView";
    }

    @PostMapping("/upload-profile-picture")
    public String uploadProfilePicture( @RequestParam("file") MultipartFile file,
                                       HttpSession session, Model model) {
        try {
            String fileName = file.getOriginalFilename();
            String uploadDir = "static/images/";
            System.out.println(uploadDir);
            System.out.println(fileName);

            String relativeUrl = "/images/" + fileName;
            userService.saveProfilePictureUrl(
                    authenticationHelper.tryGetCurrentUser(session).getUsername(), relativeUrl);
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                System.out.println(filePath);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            return "AccessDeniedView";
        }

        return "redirect:/";
    }
}