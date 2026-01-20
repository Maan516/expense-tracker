package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpSession;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.service.CategoryService;
import org.example.expensetracker.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DashboardController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public DashboardController(
            ExpenseService expenseService,
            CategoryService categoryService
    ) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        // ------------------------
        // DASHBOARD DATA
        // ------------------------
        Double totalExpense =
                expenseService.getTotalExpense(user);

        Double thisMonthExpense =
                expenseService.getThisMonthExpense(user);

        int categoryCount =
                categoryService.getCategoriesByUser(user).size();

        var recentExpenses =
                expenseService.getRecentExpenses(user);

        // ------------------------
        // MODEL
        // ------------------------
        model.addAttribute("user", user);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("thisMonthExpense", thisMonthExpense);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("recentExpenses", recentExpenses);

        // login toast (once)
        String successMsg =
                (String) session.getAttribute("loginSuccess");
        if (successMsg != null) {
            model.addAttribute("successMessage", successMsg);
            session.removeAttribute("loginSuccess");
        }

        return "dashboard";
    }
}
