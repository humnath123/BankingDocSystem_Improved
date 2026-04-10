package com.budgettracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Database Initializer - Creates database and tables if they don't exist
 */
public class DatabaseInitializer {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "humnath123@$ASD";
    private static final String DATABASE_NAME = "prashant";

    public static void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create database if it doesn't exist
            createDatabaseIfNotExists();
            
            // Create tables if they don't exist
            createTablesIfNotExist();
            
            System.out.println("Database initialization completed successfully.");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createDatabaseIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            String createDbSQL = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            stmt.executeUpdate(createDbSQL);
            System.out.println("Database '" + DATABASE_NAME + "' ensured to exist.");
        }
    }

    private static void createTablesIfNotExist() throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL + "/" + DATABASE_NAME, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Users table
            String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "phone VARCHAR(20)," +
                    "full_name VARCHAR(100)," +
                    "role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(usersTable);
            System.out.println("Table 'users' ensured to exist.");

            // Categories table
            String categoriesTable = "CREATE TABLE IF NOT EXISTS categories (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL UNIQUE" +
                    ")";
            stmt.executeUpdate(categoriesTable);
            System.out.println("Table 'categories' ensured to exist.");

            // Budgets table
            String budgetsTable = "CREATE TABLE IF NOT EXISTS budgets (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "category_id INT," +
                    "limit_amount DECIMAL(10,2) NOT NULL," +
                    "month INT NOT NULL," +
                    "year INT NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)," +
                    "FOREIGN KEY (category_id) REFERENCES categories(id)," +
                    "UNIQUE KEY unique_budget (user_id, category_id, month, year)" +
                    ")";
            stmt.executeUpdate(budgetsTable);
            System.out.println("Table 'budgets' ensured to exist.");

            // Transactions table
            String transactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "amount DECIMAL(10,2) NOT NULL," +
                    "type ENUM('INCOME', 'EXPENSE') NOT NULL," +
                    "category_id INT," +
                    "date DATE NOT NULL," +
                    "note TEXT," +
                    "user_id INT NOT NULL," +
                    "FOREIGN KEY (category_id) REFERENCES categories(id)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(transactionsTable);
            System.out.println("Table 'transactions' ensured to exist.");

            // Saving Goals table
            String savingGoalsTable = "CREATE TABLE IF NOT EXISTS saving_goals (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "goal_name VARCHAR(100) NOT NULL," +
                    "target_amount DECIMAL(10,2) NOT NULL," +
                    "current_amount DECIMAL(10,2) DEFAULT 0," +
                    "deadline DATE," +
                    "status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")";
            stmt.executeUpdate(savingGoalsTable);
            System.out.println("Table 'saving_goals' ensured to exist.");

            // Saving Transactions table
            String savingTransactionsTable = "CREATE TABLE IF NOT EXISTS saving_transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "goal_id INT NOT NULL," +
                    "user_id INT NOT NULL," +
                    "amount DECIMAL(10,2) NOT NULL," +
                    "date DATE NOT NULL," +
                    "note TEXT," +
                    "FOREIGN KEY (goal_id) REFERENCES saving_goals(id)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(savingTransactionsTable);
            System.out.println("Table 'saving_transactions' ensured to exist.");

            // Insert default categories if empty
            insertDefaultCategories(stmt);
            
            // Insert default admin user if not exists
            insertDefaultAdminUser(stmt);
        }
    }

    private static void insertDefaultCategories(Statement stmt) throws SQLException {
        String checkCategoriesSQL = "SELECT COUNT(*) as count FROM categories";
        try (var rs = stmt.executeQuery(checkCategoriesSQL)) {
            if (rs.next() && rs.getInt("count") == 0) {
                String insertCategoriesSQL = "INSERT INTO categories (name) VALUES " +
                        "('Food'), ('Rent'), ('Travel'), ('Bills'), ('Entertainment'), " +
                        "('Salary'), ('Healthcare'), ('Education'), ('Other')";
                stmt.executeUpdate(insertCategoriesSQL);
                System.out.println("Default categories inserted.");
            }
        }
    }

    private static void insertDefaultAdminUser(Statement stmt) throws SQLException {
        String checkAdminSQL = "SELECT COUNT(*) as count FROM users WHERE role='ADMIN'";
        try (var rs = stmt.executeQuery(checkAdminSQL)) {
            if (rs.next() && rs.getInt("count") == 0) {
                String insertAdminSQL = "INSERT INTO users (username, password, email, phone, full_name, role) " +
                        "VALUES ('admin', 'admin1223', 'admin@budgettracker.com', '9999999999', 'Admin User', 'ADMIN')";
                stmt.executeUpdate(insertAdminSQL);
                System.out.println("Default admin user inserted.");
            }
        }
    }
}


