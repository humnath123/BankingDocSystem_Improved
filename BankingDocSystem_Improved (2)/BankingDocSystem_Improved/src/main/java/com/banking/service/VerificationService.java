package com.banking.service;

import com.banking.util.Session;

/**
 * VerificationService - Handles two-step authentication for Admin and Staff users
 * Manages passkey verification with attempt limits and security measures
 */
public class VerificationService {

    // Fixed passkey for Admin/Staff verification
    private static final String ADMIN_STAFF_PASSKEY = "9847037778";

    // Maximum verification attempts allowed
    private static final int MAX_ATTEMPTS = 3;

    // Current attempt count for the session
    private int currentAttempts = 0;

    // Singleton instance
    private static VerificationService instance;

    private VerificationService() {}

    public static VerificationService getInstance() {
        if (instance == null) {
            instance = new VerificationService();
        }
        return instance;
    }

    /**
     * Verifies the entered passkey against the stored value
     * @param enteredPasskey The passkey entered by the user
     * @return VerificationResult containing success status and message
     */
    public VerificationResult verifyPasskey(String enteredPasskey) {
        // Check if user has exceeded maximum attempts
        if (currentAttempts >= MAX_ATTEMPTS) {
            return new VerificationResult(false,
                "Maximum verification attempts exceeded. Please contact system administrator.",
                true); // blocked
        }

        // Increment attempt counter
        currentAttempts++;

        // Check if passkey is empty
        if (enteredPasskey == null || enteredPasskey.trim().isEmpty()) {
            return new VerificationResult(false,
                "Passkey is required. Attempts remaining: " + (MAX_ATTEMPTS - currentAttempts),
                false);
        }

        // Verify passkey
        if (ADMIN_STAFF_PASSKEY.equals(enteredPasskey.trim())) {
            // Success - reset attempts for next login
            resetAttempts();
            return new VerificationResult(true, "Verification successful!", false);
        } else {
            // Failed - check if max attempts reached
            if (currentAttempts >= MAX_ATTEMPTS) {
                return new VerificationResult(false,
                    "Incorrect passkey. Maximum attempts exceeded. Access blocked.",
                    true); // blocked
            } else {
                return new VerificationResult(false,
                    "Incorrect passkey. Attempts remaining: " + (MAX_ATTEMPTS - currentAttempts),
                    false);
            }
        }
    }

    /**
     * Checks if the current user requires two-step verification
     * @return true if user is Admin or Staff, false if Customer
     */
    public boolean requiresVerification() {
        Session session = Session.getInstance();
        return session.isAdmin() || session.isStaff();
    }

    /**
     * Gets the remaining attempts for the current session
     * @return number of attempts remaining
     */
    public int getRemainingAttempts() {
        return Math.max(0, MAX_ATTEMPTS - currentAttempts);
    }

    /**
     * Resets the attempt counter (called on successful verification or new login)
     */
    public void resetAttempts() {
        currentAttempts = 0;
    }

    /**
     * Checks if the user is currently blocked due to exceeded attempts
     * @return true if blocked, false otherwise
     */
    public boolean isBlocked() {
        return currentAttempts >= MAX_ATTEMPTS;
    }

    /**
     * Gets the maximum allowed attempts
     * @return maximum attempts
     */
    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }

    /**
     * Inner class to represent verification result
     */
    public static class VerificationResult {
        private final boolean success;
        private final String message;
        private final boolean blocked;

        public VerificationResult(boolean success, String message, boolean blocked) {
            this.success = success;
            this.message = message;
            this.blocked = blocked;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public boolean isBlocked() { return blocked; }
    }
}
