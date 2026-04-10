package com.budgettracker.controller;

import com.budgettracker.service.TransactionService;
import com.budgettracker.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

public class UserReportsController implements Initializable {
    @FXML private Label monthlyIncomeLabel;
    @FXML private Label monthlyExpenseLabel;
    @FXML private Label monthlySavingsLabel;
    @FXML private TableView<?> categoryTable;

    private TransactionService transactionService = new TransactionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadReports();
    }

    private void loadReports() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        YearMonth now = YearMonth.now();
        
        // Calculate monthly income
        double monthlyIncome = transactionService.getTransactionsByUser(userId)
            .stream()
            .filter(t -> "INCOME".equals(t.getType().name()))
            .mapToDouble(t -> t.getAmount())
            .sum();
        monthlyIncomeLabel.setText(String.format("₹%.2f", monthlyIncome));
        
        // Calculate monthly expense
        double monthlyExpense = transactionService.calculateMonthlyExpense(userId, now.getMonthValue(), now.getYear());
        monthlyExpenseLabel.setText(String.format("₹%.2f", monthlyExpense));
        
        // Calculate monthly savings
        double monthlySavings = monthlyIncome - monthlyExpense;
        monthlySavingsLabel.setText(String.format("₹%.2f", monthlySavings));
    }
}
