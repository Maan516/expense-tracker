package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpSession;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    // ================= SHOW PROFILE =================
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", user);
        return "profile";
    }
    @PostMapping("/profile/password")
    public String changePassword(
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        String result = userService.changePassword(
                user,
                currentPassword,
                newPassword,
                confirmPassword
        );

        // ❌ ERROR → TOAST
        if (!"SUCCESS".equals(result)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    result
            );
            return "redirect:/profile";
        }

        // ✅ SUCCESS → TOAST
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Password updated successfully. Please login again."
        );

        // Logout after password change
        session.invalidate();

        return "redirect:/users/login";
    }
}

