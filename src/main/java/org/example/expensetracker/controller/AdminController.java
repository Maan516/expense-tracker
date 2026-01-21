package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpSession;
import org.example.expensetracker.entity.Role;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ===============================
    // ADMIN DASHBOARD
    // ===============================
    @GetMapping("/dashboard")
    public String adminDashboard(
            HttpSession session,
            Model model
    ) {

        User admin = (User) session.getAttribute("loggedInUser");

        if (admin == null) {
            return "redirect:/users/login";
        }

        if (admin.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        model.addAttribute("admin", admin);
        model.addAttribute(
                "totalUsers",
                adminService.getTotalUsers()
        );

        return "admin/dashboard";
    }

    // ===============================
    // ADMIN → USERS LIST
    // ===============================
    @GetMapping("/users")
    public String adminUsers(
            HttpSession session,
            Model model
    ) {

        User admin = (User) session.getAttribute("loggedInUser");

        if (admin == null) {
            return "redirect:/users/login";
        }

        if (admin.getRole() != Role.ADMIN) {
            return "redirect:/dashboard";
        }

        model.addAttribute(
                "users",
                adminService.getAllNormalUsers()
        );

        return "admin/users";
    }
}
