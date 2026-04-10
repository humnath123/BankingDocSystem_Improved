package com.budgettracker.service;

import com.budgettracker.dao.BudgetDaoImpl;
import com.budgettracker.dao.TransactionDaoImpl;
import com.budgettracker.model.Budget;
import com.budgettracker.model.Transaction;
import com.budgettracker.model.TransactionType;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class BudgetService {
    private BudgetDaoImpl budgetDao = new BudgetDaoImpl();
    private TransactionDaoImpl transactionDao = new TransactionDaoImpl();

    public void createBudget(Budget budget) {
        if (validateBudget(budget)) {
            budgetDao.create(budget);
        }
    }

    public void updateBudget(Budget budget) {
        if (validateBudget(budget)) {
            budgetDao.update(budget);
        }
    }

    public void deleteBudget(int budgetId) {
        budgetDao.delete(budgetId);
    }

    public List<Budget> getBudgetsByUser(int userId) {
        return budgetDao.findByUserId(userId);
    }

    public List<Budget> getBudgetsByUserAndMonth(int userId, int month, int year) {
        return budgetDao.findByUserIdAndMonth(userId, month, year);
    }

    public double getBudgetUsage(int userId, int categoryId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Transaction> transactions = transactionDao.findByUserIdAndDateRange(userId, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end));
        
        double spent = 0;
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE && t.getCategory().getId() == categoryId) {
                spent += t.getAmount();
            }
        }
        return spent;
    }

    public double getBudgetPercentage(int userId, int categoryId, int month, int year) {
        Budget budget = budgetDao.findByUserIdAndMonth(userId, month, year)
            .stream()
            .filter(b -> b.getCategoryId() == categoryId)
            .findFirst()
            .orElse(null);

        if (budget == null) return 0;
        
        double spent = getBudgetUsage(userId, categoryId, month, year);
        return (spent / budget.getLimitAmount()) * 100;
    }

    public boolean isBudgetExceeded(int userId, int categoryId, int month, int year) {
        return getBudgetPercentage(userId, categoryId, month, year) > 100;
    }

    private boolean validateBudget(Budget budget) {
        return budget.getLimitAmount() > 0 && budget.getUserId() > 0;
    }
}
