package com.budgettracker.controller;

import com.budgettracker.model.Transaction;
import com.budgettracker.service.TransactionService;
import com.budgettracker.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML private Label userLabel;
    @FXML private Label balanceLabel;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> noteColumn;

    private TransactionService transactionService = new TransactionService();
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLabel.setText(SessionManager.getInstance().getCurrentUser().getUsername());
        loadTransactions();
        updateBalance();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().name()));
        categoryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory().getName()));
        dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate().toString()));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    private void loadTransactions() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        transactionList.setAll(transactionService.getTransactionsByUser(userId));
        transactionTable.setItems(transactionList);
    }

    private void updateBalance() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        double balance = transactionService.calculateBalance(userId);
        balanceLabel.setText(String.format("%.2f", balance));
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.getInstance().logout();
        loadLogin();
    }

    @FXML
    private void handleAddTransaction(ActionEvent event) {
        loadTransactionForm(null);
    }

    @FXML
    private void handleEditTransaction(ActionEvent event) {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            loadTransactionForm(selected);
        }
    }

    @FXML
    private void handleDeleteTransaction(ActionEvent event) {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            transactionService.deleteTransaction(selected.getId());
            loadTransactions();
            updateBalance();
        }
    }

    private void loadLogin() {
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

    private void loadTransactionForm(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TransactionForm.fxml"));
            Parent root = loader.load();
            TransactionController controller = loader.getController();
            controller.setTransaction(transaction);
            controller.setDashboardController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(transaction == null ? "Add Transaction" : "Edit Transaction");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        loadTransactions();
        updateBalance();
    }
}
