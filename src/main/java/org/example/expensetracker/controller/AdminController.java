package org.example.expensetracker.controller;

import org.example.expensetracker.entity.Role;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.example.expensetracker.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    public AdminController(AdminService adminService, UserRepository userRepository) {
        this.adminService = adminService;
        this.userRepository = userRepository;
    }

    // ===============================
    // ADMIN DASHBOARD
    // ===============================
    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {

        // ✅ logged in email
        String email = authentication.getName();

        // ✅ fetch admin from DB
        User admin = userRepository.findByEmail(email).orElse(null);

        if (admin == null) {
            return "redirect:/users/login";
        }

        if (admin.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        model.addAttribute("admin", admin);
        model.addAttribute("totalUsers", adminService.getTotalUsers());

        return "admin/dashboard";
    }

    // ===============================
    // ADMIN → USERS LIST
    // ===============================
    @GetMapping("/users")
    public String adminUsers(Authentication authentication, Model model) {

        String email = authentication.getName();

        User admin = userRepository.findByEmail(email).orElse(null);

        if (admin == null) {
            return "redirect:/users/login";
        }

        if (admin.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        model.addAttribute("users", adminService.getAllNormalUsers());

        return "admin/users";
    }
}
