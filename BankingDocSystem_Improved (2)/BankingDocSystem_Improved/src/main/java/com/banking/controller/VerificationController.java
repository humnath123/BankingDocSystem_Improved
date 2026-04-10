package com.banking.controller;

import com.banking.service.VerificationService;
import com.banking.util.Session;
import com.banking.view.DashboardView;
import com.banking.view.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * VerificationController - Handles two-step authentication logic
 * Manages the verification process for Admin and Staff users
 */
public class VerificationController {

    private final VerificationService verificationService;
    private final Stage stage;

    public VerificationController(Stage stage) {
        this.verificationService = VerificationService.getInstance();
        this.stage = stage;
    }

    /**
     * Verifies the entered passkey and handles navigation
     * @param passkey The passkey entered by the user
     * @return VerificationResult containing success status and message
     */
    public VerificationService.VerificationResult verifyPasskey(String passkey) {
        VerificationService.VerificationResult result = verificationService.verifyPasskey(passkey);

        if (result.isSuccess()) {
            // Verification successful - navigate to dashboard
            navigateToDashboard();
        } else if (result.isBlocked()) {
            // User is blocked - navigate back to login after a delay
            // In a real application, you might want to log this security event
            navigateBackToLogin();
        }
        // If failed but not blocked, just return the result to show error

        return result;
    }

    /**
     * Gets the remaining verification attempts
     * @return number of attempts remaining
     */
    public int getRemainingAttempts() {
        return verificationService.getRemainingAttempts();
    }

    /**
     * Checks if verification is required for the current user
     * @return true if Admin or Staff, false if Customer
     */
    public boolean requiresVerification() {
        return verificationService.requiresVerification();
    }

    /**
     * Checks if the current user is blocked due to exceeded attempts
     * @return true if blocked, false otherwise
     */
    public boolean isBlocked() {
        return verificationService.isBlocked();
    }

    /**
     * Gets the maximum allowed attempts
     * @return maximum attempts
     */
    public int getMaxAttempts() {
        return verificationService.getMaxAttempts();
    }

    /**
     * Navigates to the dashboard after successful verification
     */
    private void navigateToDashboard() {
        DashboardView dashboard = new DashboardView(stage);
        Scene scene = new Scene(dashboard.getRoot(), 1100, 700);
        scene.getStylesheets().add(
            getClass().getResource("/com/banking/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setWidth(1100);
        stage.setHeight(700);
        stage.centerOnScreen();
    }

    /**
     * Navigates back to login screen when verification fails or is blocked
     */
    private void navigateBackToLogin() {
        // Reset the session to clear the authenticated user
        Session.getInstance().logout();

        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 1200, 700);
        scene.getStylesheets().add(
            getClass().getResource("/com/banking/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(700);
        stage.centerOnScreen();
    }

    /**
     * Resets the verification attempts (useful for testing or admin override)
     */
    public void resetAttempts() {
        verificationService.resetAttempts();
    }
}
