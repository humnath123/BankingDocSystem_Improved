package com.budgettracker.controller;

import com.budgettracker.model.Transaction;
import com.budgettracker.service.TransactionService;
import com.budgettracker.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

public class UserOverviewController implements Initializable {
    @FXML private Label balanceLabel;
    @FXML private Label monthlyExpenseLabel;
    @FXML private Label totalIncomeLabel;
    @FXML private TableView<Transaction> recentTransactionsTable;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;

    private TransactionService transactionService = new TransactionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshData();
    }

    private void refreshData() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        
        // Calculate balance
        double balance = transactionService.calculateBalance(userId);
        balanceLabel.setText(String.format("₹%.2f", balance));

        // Calculate monthly expense
        YearMonth now = YearMonth.now();
        double monthlyExpense = transactionService.calculateMonthlyExpense(userId, now.getMonthValue(), now.getYear());
        monthlyExpenseLabel.setText(String.format("₹%.2f", monthlyExpense));

        // Calculate total income
        double totalIncome = transactionService.getTransactionsByUser(userId)
            .stream()
            .filter(t -> "INCOME".equals(t.getType().name()))
            .mapToDouble(t -> t.getAmount())
            .sum();
        totalIncomeLabel.setText(String.format("₹%.2f", totalIncome));
    }
}
