package com.budgettracker.controller;

import com.budgettracker.dao.UserDaoImpl;
import com.budgettracker.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageUsersController implements Initializable {
    @FXML private TableView<User> usersTable;

    private UserDaoImpl userDao = new UserDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUsers();
    }

    private void loadUsers() {
        List<User> users = userDao.findAll();
        usersTable.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    private void handleDeleteUser() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected != null && !"ADMIN".equals(selected.getRole())) {
            userDao.delete(selected.getId());
            loadUsers();
            showAlert("Success", "User deleted successfully!");
        } else {
            showAlert("Error", "Cannot delete admin users or no user selected");
        }
    }

    @FXML
    private void handleRefresh() {
        loadUsers();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
