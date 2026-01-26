package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.example.expensetracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;
    private final UserRepository userRepository;

    public ProfileController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ================= SHOW PROFILE =================
    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        String result = userService.changePassword(
                user,
                currentPassword,
                newPassword,
                confirmPassword
        );

        // ERROR → TOAST
        if (!"SUCCESS".equals(result)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    result
            );
            return "redirect:/profile";
        }

        // SUCCESS → TOAST
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Password updated successfully. Please login again."
        );

        // Logout after password change (Spring Security way)
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/users/login";
    }
}
