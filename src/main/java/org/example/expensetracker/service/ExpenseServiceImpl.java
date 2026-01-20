package org.example.expensetracker.service;
import org.example.expensetracker.entity.Expense;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    // CONSTRUCTOR INJECTION
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUserWithCategory(user);
    }


    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }
    @Override
    public Page<Expense> getFilteredExpenses(
            User user,
            Long categoryId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable) {

        return expenseRepository.findFilteredExpenses(
                user, categoryId, fromDate, toDate, pageable
        );
    }
    @Override
    public Double getTotalExpense(User user) {
        return expenseRepository.getTotalExpense(user);
    }

    @Override
    public Double getThisMonthExpense(User user) {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();
        return expenseRepository.getTotalExpenseByDateRange(user, start, end);
    }

    @Override
    public List<Expense> getRecentExpenses(User user) {
        Pageable pageable = PageRequest.of(0, 5);
        return expenseRepository.findRecentExpenses(user, pageable);
    }


}



