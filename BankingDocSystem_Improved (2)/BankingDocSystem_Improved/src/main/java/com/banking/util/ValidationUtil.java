package com.banking.util;

/**
 * ValidationUtil — Centralized validation logic for all forms
 * Ensures consistent validation across the application
 */
public final class ValidationUtil {

    private ValidationUtil() {}

    /**
     * Validate phone number: must be numeric and exactly 10 characters
     * @param phone Phone number to validate
     * @return Error message if invalid, null if valid
     */
    public static String validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return "Phone number is required.";
        }
        if (!phone.matches("\\d{10}")) {
            return "Phone number must be exactly 10 digits (numeric only).";
        }
        return null; // valid
    }

    /**
     * Validate name: must not contain numbers
     * @param name Name to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateName(String name) {
        if (name == null || name.isBlank()) {
            return "Name is required.";
        }
        if (name.matches(".*\\d.*")) {
            return "Name should not contain numbers.";
        }
        return null; // valid
    }

    /**
     * Validate username: must contain letters and numbers, no whitespace
     * @param username Username to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateUsername(String username) {
        if (username == null || username.isBlank()) {
            return "Username is required.";
        }
        if (username.length() < 4) {
            return "Username must be at least 4 characters.";
        }
        if (username.contains(" ") || username.contains("\t")) {
            return "Username cannot contain whitespace.";
        }
        if (!username.matches(".*[a-zA-Z].*")) {
            return "Username must contain at least one letter.";
        }
        if (!username.matches(".*[0-9].*")) {
            return "Username must contain at least one number.";
        }
        return null; // valid
    }

    /**
     * Validate address: must not be empty and have minimum length
     * @param address Address to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateAddress(String address) {
        if (address == null || address.isBlank()) {
            return "Address is required.";
        }
        if (address.length() < 5) {
            return "Address must be at least 5 characters long.";
        }
        return null; // valid
    }

    /**
     * Validate email format
     * @param email Email to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return "Email is required.";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email format.";
        }
        return null; // valid
    }

    /**
     * Validate password
     * @param password Password to validate
     * @return Error message if invalid, null if valid
     */
    public static String validatePassword(String password) {
        if (password == null || password.isBlank()) {
            return "Password is required.";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        return null; // valid
    }

    /**
     * Validate date of birth: cannot be in future and must be at least 10 years old
     * @param dob Date of birth in YYYY-MM-DD format
     * @return Error message if invalid, null if valid
     */
    public static String validateDateOfBirth(String dob) {
        if (dob == null || dob.isBlank()) {
            return null; // DOB is optional
        }
        
        // Validate format
        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "Date of birth must be in YYYY-MM-DD format (e.g. 1998-05-15).";
        }
        
        try {
            String[] parts = dob.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            
            // Validate month and day ranges
            if (month < 1 || month > 12) {
                return "Month must be between 1 and 12.";
            }
            if (day < 1 || day > 31) {
                return "Day must be between 1 and 31.";
            }
            
            // Current year is 2026
            int currentYear = 2026;
            int currentMonth = 4;  // April
            int currentDay = 10;   // 10th
            
            // Check if in future
            if (year > currentYear) {
                return "Date of birth cannot be in the future.";
            }
            if (year == currentYear && month > currentMonth) {
                return "Date of birth cannot be in the future.";
            }
            if (year == currentYear && month == currentMonth && day > currentDay) {
                return "Date of birth cannot be in the future.";
            }
            
            // Check if at least 10 years old
            int age = currentYear - year;
            if (month > currentMonth || (month == currentMonth && day > currentDay)) {
                age--;
            }
            if (age < 10) {
                return "You must be at least 10 years old.";
            }
            
            return null; // valid
        } catch (Exception e) {
            return "Invalid date of birth format.";
        }
    }
}

