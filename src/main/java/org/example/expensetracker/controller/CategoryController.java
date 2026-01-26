package org.example.expensetracker.controller;

import jakarta.validation.Valid;
import org.example.expensetracker.entity.Category;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.example.expensetracker.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public CategoryController(CategoryService categoryService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    // ===============================
    // SHOW CATEGORY PAGE
    // ===============================
    @GetMapping
    public String showCategoryPage(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("category", new Category());

        // ✅ REQUIRED (topbar / templates use ${user.name})
        model.addAttribute("user", user);

        model.addAttribute(
                "categories",
                categoryService.getCategoriesByUser(user)
        );

        return "category";
    }

    // ===============================
    // ADD CATEGORY
    // ===============================
    @PostMapping("/add")
    public String addCategory(
            @Valid @ModelAttribute("category") Category category,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        // ❌ Validation error → redirect + toast
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Category name is required"
            );
            return "redirect:/categories";
        }

        // ❌ Duplicate category → redirect + toast
        if (categoryService.categoryExists(category.getName(), user)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Category already exists"
            );
            return "redirect:/categories";
        }

        // ✅ Save
        category.setUser(user);
        categoryService.saveCategory(category);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Category added successfully"
        );

        return "redirect:/categories";
    }

    // ===============================
    // UPDATE CATEGORY
    // ===============================
    @PostMapping("/update")
    public String updateCategory(
            @Valid @ModelAttribute Category category,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/users/login";
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Category name cannot be empty"
            );
            return "redirect:/categories";
        }

        category.setUser(user);
        categoryService.saveCategory(category);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Category updated successfully"
        );

        return "redirect:/categories";
    }

    // ===============================
    // DELETE CATEGORY
    // ===============================
    @GetMapping("/delete/{id}")
    public String deleteCategory(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        categoryService.deleteCategory(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Category deleted successfully"
        );

        return "redirect:/categories";
    }
}
