package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.models.User;
import com.example.forum.models.enums.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage() {
        return "HomeView";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "AboutView";
    }

    @GetMapping("/admin")
    public String showAdminPortal(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);

            if (user.getRole() == Role.ADMIN) {
                return "AdminPortalView";
            }

            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/user/{id}")
    public String showUserProfile(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);

            if (user.getRole() == Role.USER) {
                return "UserProfileView";

            } else if (user.getRole() == Role.ADMIN) {
                return "AdminPortalView";
            }

            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "ErrorView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
