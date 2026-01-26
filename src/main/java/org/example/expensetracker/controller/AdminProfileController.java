package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.expensetracker.entity.Role;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.example.expensetracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AdminProfileController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ===============================
    // ADMIN PROFILE PAGE
    // ===============================
    @GetMapping
    public String adminProfile(Authentication authentication, Model model) {

        String email = authentication.getName();

        User admin = userRepository.findByEmail(email).orElse(null);
        if (admin == null) {
            return "redirect:/users/login";
        }

        if (admin.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        model.addAttribute("admin", admin);
        return "admin/profile";
    }

    // ===============================
    // CHANGE PASSWORD
    // ===============================
    @PostMapping("/password")
    public String changePassword(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {

        String email = authentication.getName();

        User admin = userRepository.findByEmail(email).orElse(null);
        if (admin == null) {
            return "redirect:/users/login";
        }

        if (admin.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        String result = userService.changePassword(
                admin,
                currentPassword,
                newPassword,
                confirmPassword
        );

        if (!"SUCCESS".equals(result)) {
            redirectAttributes.addFlashAttribute("errorMessage", result);
            return "redirect:/admin/profile";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Password updated successfully. Please login again."
        );

        // ✅ Logout after password change (security)
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/users/login";
    }
}
