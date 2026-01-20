package org.example.expensetracker.repository;

import org.example.expensetracker.entity.Category;
import org.example.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Get all categories of a user
    List<Category> findByUser(User user);

    // Check duplicate category for same user
    boolean existsByNameAndUser(String name, User user);

    Optional<Category> findByIdAndUser(Long id, User user);

    // Find category by name & user (used later in expense)
    Optional<Category> findByNameAndUser(String name, User user);


}
