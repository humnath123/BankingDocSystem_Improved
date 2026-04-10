package com.budgettracker.dao;

import com.budgettracker.model.Transaction;
import java.util.List;

public interface TransactionDao {
    void create(Transaction transaction);
    Transaction findById(int id);
    List<Transaction> findByUserId(int userId);
    List<Transaction> findByUserIdAndCategory(int userId, int categoryId);
    List<Transaction> findByUserIdAndDateRange(int userId, java.sql.Date startDate, java.sql.Date endDate);
    void update(Transaction transaction);
    void delete(int id);
}
