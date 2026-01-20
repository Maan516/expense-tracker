package org.example.expensetracker.repository;


import org.example.expensetracker.entity.Expense;
import org.example.expensetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("""
        SELECT e FROM Expense e
        JOIN FETCH e.category
        WHERE e.user = :user
    """)
    List<Expense> findByUserWithCategory(@Param("user") User user);

    // Get expenses by category for a user
    List<Expense> findByUserAndCategory(User user, String category);
    @EntityGraph(attributePaths = {"category"})
    @Query("""
SELECT e
FROM Expense e
WHERE e.user = :user
  AND (:categoryId IS NULL OR e.category.id = :categoryId)
  AND e.date >= COALESCE(:fromDate, e.date)
  AND e.date <= COALESCE(:toDate, e.date)
ORDER BY e.date DESC
""")
    Page<Expense> findFilteredExpenses(
            @Param("user") User user,
            @Param("categoryId") Long categoryId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );




    @Query("""
    SELECT e
    FROM Expense e
    JOIN FETCH e.category
    WHERE e.user = :user
      AND e.date BETWEEN :fromDate AND :toDate
    ORDER BY e.date DESC
""")
    List<Expense> findByUserAndDateRange(
            @Param("user") User user,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );


    // 2️⃣ Total expense
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user = :user
          AND e.date BETWEEN :fromDate AND :toDate
    """)
    Double getTotalExpenseByDateRange(
            @Param("user") User user,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    // 📌 Total expense (ALL TIME)
    @Query("""
    SELECT COALESCE(SUM(e.amount), 0)
    FROM Expense e
    WHERE e.user = :user
""")
    Double getTotalExpense(User user);

    //Recent expenses (latest 5)
    @Query("""
    SELECT e
    FROM Expense e
    JOIN FETCH e.category
    WHERE e.user = :user
    ORDER BY e.date DESC
""")
    List<Expense> findRecentExpenses(User user, Pageable pageable);

}

