package org.example.expensetracker.service;

import org.example.expensetracker.entity.Category;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructor Injection
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategoriesByUser(User user) {
        return categoryRepository.findByUser(user);
    }

    @Override
    public boolean categoryExists(String name, User user) {
        return categoryRepository.existsByNameAndUser(name, user);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // 🔥 ADD THIS METHOD (ONLY ADDITION)
    @Override
    public Category getCategoryByIdAndUser(Long id, User user) {
        return categoryRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }


}
