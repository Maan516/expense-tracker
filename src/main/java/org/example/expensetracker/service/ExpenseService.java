package org.example.expensetracker.service;

import org.example.expensetracker.entity.Expense;
import org.example.expensetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    // Save new expense
    Expense saveExpense(Expense expense);

    // Get all expenses of a user
    List<Expense> getExpensesByUser(User user);

    // Delete expense
    void deleteExpense(Long id);
    Expense getExpenseById(Long id);

    Page<Expense> getFilteredExpenses(
            User user,
            Long categoryId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    );
    Double getTotalExpense(User user);

    Double getThisMonthExpense(User user);

    List<Expense> getRecentExpenses(User user);

}
