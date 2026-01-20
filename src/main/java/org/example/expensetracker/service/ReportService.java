package org.example.expensetracker.service;

import org.example.expensetracker.entity.User;

import java.time.LocalDate;
import java.util.Map;

    public interface ReportService {
        Map<String, Object> getDateRangeReport(
                User user,
                LocalDate fromDate,
                LocalDate toDate
        );


    }
