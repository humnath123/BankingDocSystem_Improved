package com.budgettracker.controller;

import com.budgettracker.model.SavingGoal;
import com.budgettracker.service.SavingGoalService;
import com.budgettracker.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class SavingGoalsController implements Initializable {
    @FXML private TableView<SavingGoal> goalsTable;
    @FXML private TableColumn<SavingGoal, String> goalNameColumn;
    @FXML private TableColumn<SavingGoal, Double> targetColumn;
    @FXML private TableColumn<SavingGoal, Double> currentColumn;
    @FXML private TableColumn<SavingGoal, Double> progressColumn;
    @FXML private TableColumn<SavingGoal, LocalDate> deadlineColumn;
    @FXML private TableColumn<SavingGoal, String> statusColumn;

    private SavingGoalService goalService = new SavingGoalService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadGoals();
    }

    private void loadGoals() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        List<SavingGoal> goals = goalService.getUserGoals(userId);
        goalsTable.setItems(FXCollections.observableArrayList(goals));
    }

    @FXML
    private void handleCreateGoal() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Saving Goal");
        
        TextField goalNameField = new TextField();
        goalNameField.setPromptText("Goal Name");
        
        TextField targetField = new TextField();
        targetField.setPromptText("Target Amount");
        
        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setValue(LocalDate.now().plusMonths(6));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Goal Name:"), 0, 0);
        grid.add(goalNameField, 1, 0);
        grid.add(new Label("Target Amount:"), 0, 1);
        grid.add(targetField, 1, 1);
        grid.add(new Label("Deadline:"), 0, 2);
        grid.add(deadlinePicker, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        java.util.Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String goalName = goalNameField.getText();
                double target = Double.parseDouble(targetField.getText());
                LocalDate deadline = deadlinePicker.getValue();
                
                if (goalName.isEmpty()) {
                    showAlert("Error", "Goal name cannot be empty");
                    return;
                }
                
                if (target <= 0) {
                    showAlert("Error", "Target amount must be greater than 0");
                    return;
                }
                
                SavingGoal goal = new SavingGoal();
                goal.setUserId(SessionManager.getInstance().getCurrentUser().getId());
                goal.setGoalName(goalName);
                goal.setTargetAmount(target);
                goal.setCurrentAmount(0);
                goal.setDeadline(deadline);
                goal.setStatus("ACTIVE");
                
                goalService.createGoal(goal);
                loadGoals();
                showAlert("Success", "Goal created successfully!");
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid amount - please enter a valid number");
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAddSaving() {
        SavingGoal selected = goalsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Saving");
            
            TextField amountField = new TextField();
            amountField.setPromptText("Amount to save");
            
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(new Label("Amount:"), 0, 0);
            grid.add(amountField, 1, 0);
            
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            java.util.Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        showAlert("Error", "Amount must be greater than 0");
                        return;
                    }
                    goalService.addSavingAmount(selected, amount);
                    loadGoals();
                    showAlert("Success", "Saving added successfully!");
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid amount - please enter a valid number");
                } catch (Exception e) {
                    showAlert("Error", "An error occurred: " + e.getMessage());
                }
            }
        } else {
            showAlert("Warning", "Please select a goal");
        }
    }

    @FXML
    private void handleDelete() {
        SavingGoal selected = goalsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            goalService.deleteGoal(selected.getId());
            loadGoals();
            showAlert("Success", "Goal deleted successfully!");
        } else {
            showAlert("Warning", "Please select a goal to delete");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
