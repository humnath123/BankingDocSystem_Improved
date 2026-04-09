package com.banking.controller;

import com.banking.dao.UserDAO;
import com.banking.model.User;
import com.banking.service.AuditService;
import com.banking.util.Session;
import com.banking.view.DashboardView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Controller for the login screen.
 * Delegates authentication to UserDAO (which uses BCrypt),
 * and records the login event in the audit log.
 */
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserDAO userDAO = new UserDAO();
    private final Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Authenticates and navigates on success.
     * @return null on success, or a user-facing error message on failure.
     */
    public String login(String username, String password) {
        if (username == null || username.isBlank())
            return "Username is required.";
        if (password == null || password.isBlank())
            return "Password is required.";

        Optional<User> result = userDAO.authenticate(username.trim(), password);

        if (result.isEmpty()) {
            AuditService.getInstance().log(null, "LOGIN_FAILED",
                    "User", username, "Invalid credentials attempt");
            return "Invalid username or password.";
        }

        User user = result.get();
        Session.getInstance().setCurrentUser(user);
        AuditService.getInstance().log(user.getUserId(), "LOGIN_SUCCESS",
                "User", String.valueOf(user.getUserId()), "Login from desktop app");

        log.info("User '{}' ({}) logged in.", user.getUsername(), user.getRole());

        DashboardView dashboard = new DashboardView(stage);
        Scene scene = new Scene(dashboard.getRoot(), 1200, 760);
        scene.getStylesheets().add(
                getClass().getResource("/com/banking/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(760);
        stage.centerOnScreen();
        return null;
    }
}