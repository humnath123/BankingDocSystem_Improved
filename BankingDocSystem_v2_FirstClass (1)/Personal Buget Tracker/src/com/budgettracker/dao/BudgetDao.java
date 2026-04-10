package com.budgettracker.dao;

import com.budgettracker.model.Budget;
import java.util.List;

public interface BudgetDao {
    void create(Budget budget);
    Budget findById(int id);
    List<Budget> findByUserId(int userId);
    List<Budget> findByUserIdAndMonth(int userId, int month, int year);
    void update(Budget budget);
    void delete(int id);
}
