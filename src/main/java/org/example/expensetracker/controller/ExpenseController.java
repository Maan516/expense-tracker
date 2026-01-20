package org.example.expensetracker.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.expensetracker.entity.Expense;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.service.CategoryService;
import org.example.expensetracker.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public ExpenseController(
            ExpenseService expenseService,
            CategoryService categoryService
    ) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    // ===============================
    // ADD EXPENSE PAGE
    // ===============================
    @GetMapping("/add")
    public String showAddExpensePage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        Expense expense = new Expense();
        expense.setDate(LocalDate.now());

        model.addAttribute("expense", expense);

        // 🔥 LOAD USER CATEGORIES FOR DROPDOWN
        model.addAttribute(
                "categories",
                categoryService.getCategoriesByUser(user)
        );

        return "expense-add";
    }

    // ===============================
    // SAVE EXPENSE
    // ===============================
    @PostMapping("/add")
    public String saveExpense(
            @Valid @ModelAttribute("expense") Expense expense,
            BindingResult result,
            @RequestParam("category") Long categoryId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        if (result.hasErrors()) {
            model.addAttribute(
                    "categories",
                    categoryService.getCategoriesByUser(user)
            );
            return "expense-add";
        }

        // 🔥 CONVERT ID → CATEGORY ENTITY
        expense.setCategory(
                categoryService.getCategoryByIdAndUser(categoryId, user)
        );

        expense.setUser(user);
        expenseService.saveExpense(expense);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Expense added successfully"
        );

        return "redirect:/expenses/add";
    }

    @GetMapping("/list")
    public String listExpenses(
            HttpSession session,

            // Pagination
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,

            // Filters
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,

            Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        // -------------------------
        // PAGINATION
        // -------------------------
        Pageable pageable = PageRequest.of(page, size);

        Page<Expense> expensePage =
                expenseService.getFilteredExpenses(
                        user, categoryId, fromDate, toDate, pageable
                );

        // -------------------------
        // DATA FOR TABLE
        // -------------------------
        model.addAttribute("expenses", expensePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", expensePage.getTotalPages());

        // -------------------------
        // DATA FOR MODAL + FILTER UI
        // -------------------------
        model.addAttribute(
                "categories",
                categoryService.getCategoriesByUser(user)
        );

        // IMPORTANT for edit modal
        if (!model.containsAttribute("editExpense")) {
            model.addAttribute("editExpense", new Expense());
        }

        // Keep filter values (VERY IMPORTANT UX)
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "expense-list";
    }


    // ===============================
    // UPDATE EXPENSE (WITH VALIDATION)
    // ===============================
    @PostMapping("/update")
    public String updateExpense(
            @Valid @ModelAttribute("editExpense") Expense expense,
            BindingResult result,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/users/login";
        }

        // ===============================
        // ❌ VALIDATION ERROR
        // ===============================
        if (result.hasErrors()) {

            // 🔥 REQUIRED pagination data (THIS WAS MISSING)
            int page = 0;
            int size = 5;

            Pageable pageable = PageRequest.of(page, size);

            Page<Expense> expensePage =
                    expenseService.getFilteredExpenses(
                            user, null, null, null, pageable
                    );

            // Table data
            model.addAttribute("expenses", expensePage.getContent());

            // Pagination (VERY IMPORTANT)
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", expensePage.getTotalPages());

            // Categories for dropdown
            model.addAttribute(
                    "categories",
                    categoryService.getCategoriesByUser(user)
            );

            // Reopen modal
            model.addAttribute("openEditModal", true);

            // ⚠️ DO NOT create new editExpense
            // Spring already keeps validation errors

            return "expense-list";
        }

        // ===============================
        // ✅ SUCCESS
        // ===============================
        expense.setUser(user);
        expenseService.saveExpense(expense);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Expense updated successfully"
        );

        return "redirect:/expenses/list";
    }


    // ===============================
    // DELETE EXPENSE
    // ===============================
    @GetMapping("/delete/{id}")
    public String deleteExpense(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        expenseService.deleteExpense(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Expense deleted successfully"
        );

        return "redirect:/expenses/list";
    }


}
