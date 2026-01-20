package org.example.expensetracker.service;

import org.example.expensetracker.entity.Expense;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final ExpenseRepository expenseRepository;

    public ReportServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Map<String, Object> getDateRangeReport(
            User user,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        Map<String, Object> report = new HashMap<>();

        // Table data
        List<Expense> expenses =
                expenseRepository.findByUserAndDateRange(user, fromDate, toDate);

        // Total
        Double totalExpense =
                expenseRepository.getTotalExpenseByDateRange(user, fromDate, toDate);

        report.put("expenses", expenses);
        report.put("totalExpense", totalExpense);
        report.put("fromDate", fromDate);
        report.put("toDate", toDate);

        return report;
    }
}
