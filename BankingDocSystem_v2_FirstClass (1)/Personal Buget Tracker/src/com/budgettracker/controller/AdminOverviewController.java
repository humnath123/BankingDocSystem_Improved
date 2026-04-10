package com.budgettracker.controller;

import com.budgettracker.dao.UserDaoImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminOverviewController implements Initializable {
    @FXML private Label totalUsersLabel;
    @FXML private Label totalTransactionsLabel;
    @FXML private TableView<?> usersTable;

    private UserDaoImpl userDao = new UserDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAdminData();
    }

    private void loadAdminData() {
        int totalUsers = userDao.findAll().size();
        totalUsersLabel.setText(String.valueOf(totalUsers));
    }
}
