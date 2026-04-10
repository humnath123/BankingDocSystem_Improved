package com.budgettracker.controller;

import com.budgettracker.dao.UserDaoImpl;
import com.budgettracker.model.User;
import com.budgettracker.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label fullNameError;
    @FXML private Label emailError;
    @FXML private Label phoneError;
    @FXML private Label usernameError;
    @FXML private Label passwordError;
    @FXML private Label confirmPasswordError;
    @FXML private Label generalError;

    private UserDaoImpl userDao = new UserDaoImpl();

    @FXML
    private void handleRegister() {
        clearErrors();
        boolean valid = true;

        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (!ValidationUtil.validateFullName(fullName)) {
            fullNameError.setText("Name must be 2-50 characters (letters and spaces only)");
            valid = false;
        }
        if (!ValidationUtil.validateEmail(email)) {
            emailError.setText("Invalid email format");
            valid = false;
        }
        if (!ValidationUtil.validatePhone(phone)) {
            phoneError.setText("Phone must be 10 digits");
            valid = false;
        }
        if (!ValidationUtil.validateUsername(username)) {
            usernameError.setText("Username must be 3-50 characters");
            valid = false;
        }
        if (!ValidationUtil.validatePassword(password)) {
            passwordError.setText("Password must be at least 6 characters");
            valid = false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordError.setText("Passwords do not match");
            valid = false;
        }

        if (!valid) return;

        // Check if username or email already exists
        if (userDao.findByUsername(username) != null) {
            generalError.setText("Username already taken");
            return;
        }
        if (userDao.findByEmail(email) != null) {
            generalError.setText("Email already registered");
            return;
        }

        // Create new user
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("USER");

        userDao.create(newUser);
        showAlert("Success", "Account created successfully! Please login.");
        handleBackToLogin();
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearErrors() {
        fullNameError.setText("");
        emailError.setText("");
        phoneError.setText("");
        usernameError.setText("");
        passwordError.setText("");
        confirmPasswordError.setText("");
        generalError.setText("");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
