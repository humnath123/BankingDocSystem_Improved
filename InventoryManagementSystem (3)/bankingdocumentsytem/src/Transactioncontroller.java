package com.banking.controller;

import com.banking.exception.BankingException;
import com.banking.exception.InsufficientFundsException;
import com.banking.model.Transaction;
import com.banking.service.AuditService;
import com.banking.service.TransactionService;
import com.banking.util.Session;

import java.math.BigDecimal;
import java.util.List;

/**
 * MVC Controller for transactions. Thin layer between View and Service.
 * All business logic and transaction management lives in TransactionService.
 */
public class TransactionController {

    private final TransactionService service = new TransactionService();

    public List<Transaction> getAll() {
        return service.getAllTransactions();
    }

    /**
     * Processes a deposit or withdrawal. Returns null on success, error message on failure.
     */
    public String processTransaction(String accountNumber, String type,
                                     BigDecimal amount, String description) {
        try {
            int staffId = Session.getInstance().getCurrentUser().getUserId();
            if ("Deposit".equals(type)) {
                service.deposit(accountNumber, amount, staffId, description);
                AuditService.getInstance().log(staffId, "DEPOSIT",
                        "Account", accountNumber, "Rs." + amount);
            } else if ("Withdrawal".equals(type)) {
                service.withdraw(accountNumber, amount, staffId, description);
                AuditService.getInstance().log(staffId, "WITHDRAWAL",
                        "Account", accountNumber, "Rs." + amount);
            } else {
                return "Unknown transaction type: " + type;
            }
            return null;
        } catch (InsufficientFundsException e) {
            return e.getMessage();
        } catch (BankingException e) {
            return e.getMessage();
        }
    }

    /**
     * Transfers funds atomically between two accounts.
     * Returns null on success, error message on failure.
     */
    public String transfer(String fromAccount, String toAccount, BigDecimal amount) {
        try {
            int staffId = Session.getInstance().getCurrentUser().getUserId();
            service.transfer(fromAccount, toAccount, amount, staffId);
            AuditService.getInstance().log(staffId, "TRANSFER",
                    "Account", fromAccount,
                    "Rs." + amount + " to " + toAccount);
            return null;
        } catch (InsufficientFundsException | BankingException e) {
            return e.getMessage();
        }
    }
}