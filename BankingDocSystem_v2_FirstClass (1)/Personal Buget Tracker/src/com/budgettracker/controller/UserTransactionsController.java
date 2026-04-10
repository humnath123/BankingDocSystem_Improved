package com.budgettracker.controller;

import com.budgettracker.dao.CategoryDaoImpl;
import com.budgettracker.model.*;
import com.budgettracker.service.TransactionService;
import com.budgettracker.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UserTransactionsController implements Initializable {
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> noteColumn;

    private TransactionService transactionService = new TransactionService();
    private CategoryDaoImpl categoryDao = new CategoryDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadTransactions();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        categoryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getCategory() != null ? cellData.getValue().getCategory().getName() : "N/A"));
        typeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getType().name()));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    private void loadTransactions() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        List<Transaction> transactions = transactionService.getTransactionsByUser(userId);
        ObservableList<Transaction> observableList = FXCollections.observableArrayList(transactions);
        transactionsTable.setItems(observableList);
    }

    @FXML
    private void handleAddIncome() {
        openTransactionDialog(TransactionType.INCOME);
    }

    @FXML
    private void handleAddExpense() {
        openTransactionDialog(TransactionType.EXPENSE);
    }

    @FXML
    private void handleDelete() {
        Transaction selected = transactionsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            transactionService.deleteTransaction(selected.getId());
            loadTransactions();
        } else {
            showAlert("Warning", "Please select a transaction to delete");
        }
    }

    @FXML
    private void handleRefresh() {
        loadTransactions();
    }

    private void openTransactionDialog(TransactionType type) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(type == TransactionType.INCOME ? "Add Income" : "Add Expense");
        
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        
        ComboBox<Category> categoryCombo = new ComboBox<>();
        List<Category> categories = categoryDao.findAll();
        categoryCombo.setItems(FXCollections.observableArrayList(categories));
        
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        
        TextArea noteArea = new TextArea();
        noteArea.setPromptText("Note");
        noteArea.setPrefRowCount(3);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Amount:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryCombo, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Note:"), 0, 3);
        grid.add(noteArea, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        java.util.Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String amountStr = amountField.getText().trim();
                if (amountStr.isEmpty()) {
                    showAlert("Error", "Please enter an amount");
                    return;
                }
                
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    showAlert("Error", "Amount must be greater than 0");
                    return;
                }
                
                Category category = categoryCombo.getValue();
                LocalDate date = datePicker.getValue();
                String note = noteArea.getText();
                
                if (category == null) {
                    showAlert("Error", "Please select a category");
                    return;
                }
                
                if (date == null) {
                    showAlert("Error", "Please select a date");
                    return;
                }
                
                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setType(type);
                transaction.setCategory(category);
                transaction.setDate(date);
                transaction.setNote(note);
                transaction.setUser(SessionManager.getInstance().getCurrentUser());
                
                transactionService.addTransaction(transaction);
                loadTransactions();
                showAlert("Success", "Transaction added successfully!");
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid amount - please enter a valid number");
            } catch (IllegalArgumentException e) {
                showAlert("Error", "Invalid transaction data: " + e.getMessage());
            } catch (Exception e) {
                showAlert("Error", "Failed to add transaction: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
