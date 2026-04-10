package com.budgettracker.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^[0-9]{10}$";
    private static final String NAME_REGEX = "^[a-zA-Z\\s]{2,50}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    public static boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean validatePhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean validateFullName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean validateUsername(String username) {
        return username != null && username.length() >= 3 && username.length() <= 50;
    }

    public static boolean validatePassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static String getValidationError(String field, String value) {
        switch (field.toLowerCase()) {
            case "email":
                if (!validateEmail(value)) {
                    return "Invalid email format";
                }
                break;
            case "phone":
                if (!validatePhone(value)) {
                    return "Phone number must be 10 digits";
                }
                break;
            case "fullname":
                if (!validateFullName(value)) {
                    return "Name must be 2-50 characters (letters and spaces only)";
                }
                break;
            case "username":
                if (!validateUsername(value)) {
                    return "Username must be 3-50 characters";
                }
                break;
            case "password":
                if (!validatePassword(value)) {
                    return "Password must be at least 6 characters";
                }
                break;
        }
        return null;
    }
}
