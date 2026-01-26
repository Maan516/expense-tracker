package org.example.expensetracker.controller;

import jakarta.validation.Valid;
import org.example.expensetracker.entity.Expense;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.example.expensetracker.service.CategoryService;
import org.example.expensetracker.service.ExpenseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public ExpenseController(
            ExpenseService expenseService,
            CategoryService categoryService,
            UserRepository userRepository
    ) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    // ===============================
    // ADD EXPENSE PAGE
    // ===============================
    @GetMapping("/add")
    public String showAddExpensePage(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        Expense expense = new Expense();
        expense.setDate(LocalDate.now());

        model.addAttribute("expense", expense);

        // ✅ REQUIRED for top bar username (expense-add.html uses ${user.name})
        model.addAttribute("user", user);

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
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        // ✅ REQUIRED for expense-add.html top bar (${user.name})
        model.addAttribute("user", user);

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

    // ===============================
    // LIST EXPENSES
    // ===============================
    @GetMapping("/list")
    public String listExpenses(
            Authentication authentication,

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

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        // ✅ REQUIRED for any page top bar / templates
        model.addAttribute("user", user);

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
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        // ✅ REQUIRED for expense-list page top bar
        model.addAttribute("user", user);

        // ===============================
        // ❌ VALIDATION ERROR
        // ===============================
        if (result.hasErrors()) {

            int page = 0;
            int size = 5;

            Pageable pageable = PageRequest.of(page, size);

            Page<Expense> expensePage =
                    expenseService.getFilteredExpenses(
                            user, null, null, null, pageable
                    );

            model.addAttribute("expenses", expensePage.getContent());

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", expensePage.getTotalPages());

            model.addAttribute(
                    "categories",
                    categoryService.getCategoriesByUser(user)
            );

            model.addAttribute("openEditModal", true);

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
