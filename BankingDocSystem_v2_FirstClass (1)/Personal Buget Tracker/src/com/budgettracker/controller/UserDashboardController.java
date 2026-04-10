package com.budgettracker.controller;

import com.budgettracker.service.TransactionService;
import com.budgettracker.service.BudgetService;
import com.budgettracker.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {
    @FXML private Label userLabel;
    @FXML private StackPane contentPane;

    private TransactionService transactionService = new TransactionService();
    private BudgetService budgetService = new BudgetService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLabel.setText("Welcome, " + SessionManager.getInstance().getCurrentUser().getFullName());
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadContent("/fxml/UserOverview.fxml");
    }

    @FXML
    private void showTransactions() {
        loadContent("/fxml/UserTransactions.fxml");
    }

    @FXML
    private void showBudget() {
        loadContent("/fxml/BudgetManagement.fxml");
    }

    @FXML
    private void showSavingGoals() {
        loadContent("/fxml/SavingGoals.fxml");
    }

    @FXML
    private void showReports() {
        loadContent("/fxml/UserReports.fxml");
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlFile) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
