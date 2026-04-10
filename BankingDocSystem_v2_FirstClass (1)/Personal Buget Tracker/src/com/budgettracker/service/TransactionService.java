package com.budgettracker.service;

import com.budgettracker.dao.TransactionDao;
import com.budgettracker.dao.TransactionDaoImpl;
import com.budgettracker.model.Transaction;
import com.budgettracker.model.TransactionType;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private TransactionDao transactionDao = new TransactionDaoImpl();

    public void addTransaction(Transaction transaction) {
        if (validateTransaction(transaction)) {
            transactionDao.create(transaction);
        } else {
            throw new IllegalArgumentException("Invalid transaction data");
        }
    }

    public void updateTransaction(Transaction transaction) {
        if (validateTransaction(transaction)) {
            transactionDao.update(transaction);
        } else {
            throw new IllegalArgumentException("Invalid transaction data");
        }
    }

    public void deleteTransaction(int id) {
        transactionDao.delete(id);
    }

    public List<Transaction> getTransactionsByUser(int userId) {
        return transactionDao.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByUserAndCategory(int userId, int categoryId) {
        return transactionDao.findByUserIdAndCategory(userId, categoryId);
    }

    public List<Transaction> getTransactionsByUserAndDateRange(int userId, LocalDate start, LocalDate end) {
        return transactionDao.findByUserIdAndDateRange(userId, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end));
    }

    public double calculateBalance(int userId) {
        List<Transaction> transactions = getTransactionsByUser(userId);
        double balance = 0;
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                balance += t.getAmount();
            } else {
                balance -= t.getAmount();
            }
        }
        return balance;
    }

    public double calculateMonthlyExpense(int userId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Transaction> transactions = getTransactionsByUserAndDateRange(userId, start, end);
        double expense = 0;
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                expense += t.getAmount();
            }
        }
        return expense;
    }

    private boolean validateTransaction(Transaction transaction) {
        return transaction.getAmount() > 0 && transaction.getDate() != null && transaction.getUser() != null;
    }
}
