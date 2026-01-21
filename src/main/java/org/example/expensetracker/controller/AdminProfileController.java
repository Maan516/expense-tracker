package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpSession;
import org.example.expensetracker.entity.Role;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {

    private final UserService userService;

    public AdminProfileController(UserService userService) {
        this.userService = userService;
    }

    // ===============================
    // ADMIN PROFILE PAGE
    // ===============================
    @GetMapping
    public String adminProfile(HttpSession session, Model model) {

        User admin = (User) session.getAttribute("loggedInUser");

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
            HttpSession session,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {

        User admin = (User) session.getAttribute("loggedInUser");

        if (admin == null) {
            return "redirect:/users/login";
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
                "Password updated successfully"
        );

        return "redirect:/admin/profile";
    }
}
