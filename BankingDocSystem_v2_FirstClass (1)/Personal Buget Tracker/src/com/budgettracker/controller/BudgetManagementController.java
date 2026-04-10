package com.budgettracker.controller;

import com.budgettracker.dao.CategoryDaoImpl;
import com.budgettracker.model.Budget;
import com.budgettracker.model.Category;
import com.budgettracker.service.BudgetService;
import com.budgettracker.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetManagementController implements Initializable {
    @FXML private TableView<Budget> budgetsTable;
    @FXML private TableColumn<Budget, String> categoryColumn;
    @FXML private TableColumn<Budget, Double> limitColumn;
    @FXML private TableColumn<Budget, Double> spentColumn;
    @FXML private TableColumn<Budget, Double> remainingColumn;
    @FXML private TableColumn<Budget, Double> usageColumn;
    @FXML private TableColumn<Budget, String> statusColumn;

    private BudgetService budgetService = new BudgetService();
    private CategoryDaoImpl categoryDao = new CategoryDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBudgets();
    }

    private void loadBudgets() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        YearMonth now = YearMonth.now();
        List<Budget> budgets = budgetService.getBudgetsByUserAndMonth(userId, now.getMonthValue(), now.getYear());
        budgetsTable.setItems(FXCollections.observableArrayList(budgets));
    }

    @FXML
    private void handleCreateBudget() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Budget");
        
        ComboBox<Category> categoryCombo = new ComboBox<>();
        List<Category> categories = categoryDao.findAll();
        categoryCombo.setItems(FXCollections.observableArrayList(categories));
        
        TextField limitField = new TextField();
        limitField.setPromptText("Monthly Limit Amount");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Category:"), 0, 0);
        grid.add(categoryCombo, 1, 0);
        grid.add(new Label("Limit Amount:"), 0, 1);
        grid.add(limitField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        java.util.Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String limitStr = limitField.getText().trim();
                if (limitStr.isEmpty()) {
                    showAlert("Error", "Please enter a limit amount");
                    return;
                }
                
                Category category = categoryCombo.getValue();
                double limit = Double.parseDouble(limitStr);
                
                if (limit <= 0) {
                    showAlert("Error", "Limit amount must be greater than 0");
                    return;
                }
                
                if (category == null) {
                    showAlert("Error", "Please select a category");
                    return;
                }
                
                Budget budget = new Budget();
                budget.setUserId(SessionManager.getInstance().getCurrentUser().getId());
                budget.setCategoryId(category.getId());
                budget.setLimitAmount(limit);
                budget.setMonth(YearMonth.now().getMonthValue());
                budget.setYear(YearMonth.now().getYear());
                
                budgetService.createBudget(budget);
                loadBudgets();
                showAlert("Success", "Budget created successfully!");
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid amount - please enter a valid number");
            } catch (Exception e) {
                showAlert("Error", "Failed to create budget: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEditBudget() {
        Budget selected = budgetsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Budget");
            
            TextField limitField = new TextField();
            limitField.setText(String.valueOf(selected.getLimitAmount()));
            
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(new Label("Limit Amount:"), 0, 0);
            grid.add(limitField, 1, 0);
            
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            java.util.Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String limitStr = limitField.getText().trim();
                    if (limitStr.isEmpty()) {
                        showAlert("Error", "Please enter a limit amount");
                        return;
                    }
                    
                    double newLimit = Double.parseDouble(limitStr);
                    if (newLimit <= 0) {
                        showAlert("Error", "Limit amount must be greater than 0");
                        return;
                    }
                    
                    selected.setLimitAmount(newLimit);
                    budgetService.updateBudget(selected);
                    loadBudgets();
                    showAlert("Success", "Budget updated successfully!");
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid amount - please enter a valid number");
                } catch (Exception e) {
                    showAlert("Error", "Failed to update budget: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("Warning", "Please select a budget to edit");
        }
    }

    @FXML
    private void handleDelete() {
        Budget selected = budgetsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            budgetService.deleteBudget(selected.getId());
            loadBudgets();
            showAlert("Success", "Budget deleted successfully!");
        } else {
            showAlert("Warning", "Please select a budget to delete");
        }
    }

    @FXML
    private void handleRecalculate() {
        loadBudgets();
        showAlert("Success", "Budgets recalculated!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
