package com.budgettracker.controller;

import com.budgettracker.dao.UserDaoImpl;
import com.budgettracker.model.User;
import com.budgettracker.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML private Label adminLabel;
    @FXML private StackPane contentPane;

    private UserDaoImpl userDao = new UserDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminLabel.setText("Admin: " + SessionManager.getInstance().getCurrentUser().getUsername());
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadContent("/fxml/AdminOverview.fxml");
    }

    @FXML
    private void showManageUsers() {
        loadContent("/fxml/ManageUsers.fxml");
    }

    @FXML
    private void showUserActivity() {
        loadContent("/fxml/UserActivity.fxml");
    }

    @FXML
    private void showSystemReports() {
        loadContent("/fxml/SystemReports.fxml");
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) adminLabel.getScene().getWindow();
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
