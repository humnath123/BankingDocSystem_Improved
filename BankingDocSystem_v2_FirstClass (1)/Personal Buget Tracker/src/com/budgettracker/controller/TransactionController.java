package com.budgettracker.controller;

import com.budgettracker.dao.CategoryDao;
import com.budgettracker.dao.CategoryDaoImpl;
import com.budgettracker.model.*;
import com.budgettracker.service.TransactionService;
import com.budgettracker.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    @FXML private TextField amountField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private ComboBox<Category> categoryCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextArea noteArea;
    @FXML private Label errorLabel;

    private TransactionService transactionService = new TransactionService();
    private CategoryDao categoryDao = new CategoryDaoImpl();
    private Transaction transaction;
    private DashboardController dashboardController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeCombo.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
        loadCategories();
        datePicker.setValue(LocalDate.now());
    }

    private void loadCategories() {
        List<Category> categories = categoryDao.findAll();
        ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
        categoryCombo.setItems(categoryList);
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        if (transaction != null) {
            amountField.setText(String.valueOf(transaction.getAmount()));
            typeCombo.setValue(transaction.getType().name());
            categoryCombo.setValue(transaction.getCategory());
            datePicker.setValue(transaction.getDate());
            noteArea.setText(transaction.getNote());
        }
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            TransactionType type = TransactionType.valueOf(typeCombo.getValue());
            Category category = categoryCombo.getValue();
            LocalDate date = datePicker.getValue();
            String note = noteArea.getText();
            User user = SessionManager.getInstance().getCurrentUser();

            Transaction t = new Transaction();
            t.setAmount(amount);
            t.setType(type);
            t.setCategory(category);
            t.setDate(date);
            t.setNote(note);
            t.setUser(user);

            if (transaction == null) {
                transactionService.addTransaction(t);
            } else {
                t.setId(transaction.getId());
                transactionService.updateTransaction(t);
            }

            dashboardController.refresh();
            closeWindow();
        } catch (Exception e) {
            errorLabel.setText("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.close();
    }
}
