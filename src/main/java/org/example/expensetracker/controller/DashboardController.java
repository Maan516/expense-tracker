package org.example.expensetracker.controller;

import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.example.expensetracker.service.CategoryService;
import org.example.expensetracker.service.ExpenseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public DashboardController(
            ExpenseService expenseService,
            CategoryService categoryService,
            UserRepository userRepository
    ) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            @RequestParam(value = "success", required = false) String success,
            Model model
    ) {

        // ✅ Logged in email from Spring Security
        String email = authentication.getName();

        // ✅ Fetch user from DB
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        // ------------------------
        // DASHBOARD DATA
        // ------------------------
        Double totalExpense = expenseService.getTotalExpense(user);
        Double thisMonthExpense = expenseService.getThisMonthExpense(user);
        int categoryCount = categoryService.getCategoriesByUser(user).size();
        var recentExpenses = expenseService.getRecentExpenses(user);

        // ------------------------
        // MODEL
        // ------------------------
        model.addAttribute("user", user);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("thisMonthExpense", thisMonthExpense);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("recentExpenses", recentExpenses);

        // ✅ Login toast trigger (works with dashboard.html without changing it)
        if (success != null) {
            model.addAttribute("successMessage", "Login successful");
        }

        return "dashboard";
    }
}
