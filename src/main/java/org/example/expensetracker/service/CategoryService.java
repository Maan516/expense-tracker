package org.example.expensetracker.service;

import org.example.expensetracker.entity.Category;
import org.example.expensetracker.entity.User;

import java.util.List;

public interface CategoryService {

    // Create category
    Category saveCategory(Category category);

    // Get categories of logged-in user
    List<Category> getCategoriesByUser(User user);

    Category getCategoryByIdAndUser(Long id, User user);

    // Check duplicate
    boolean categoryExists(String name, User user);

    // Delete category
    void deleteCategory(Long id);




}
