package com.budgettracker.dao;

import com.budgettracker.model.Budget;
import com.budgettracker.util.DatabaseUtil;
import com.budgettracker.util.LoggerUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDaoImpl implements BudgetDao {

    @Override
    public void create(Budget budget) {
        String sql = "INSERT INTO budgets (user_id, category_id, limit_amount, month, year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, budget.getUserId());
            stmt.setInt(2, budget.getCategoryId());
            stmt.setDouble(3, budget.getLimitAmount());
            stmt.setInt(4, budget.getMonth());
            stmt.setInt(5, budget.getYear());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                budget.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error creating budget", e);
        }
    }

    @Override
    public Budget findById(int id) {
        String sql = "SELECT b.*, c.name FROM budgets b LEFT JOIN categories c ON b.category_id = c.id WHERE b.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Budget budget = new Budget(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("category_id"),
                        rs.getDouble("limit_amount"), rs.getInt("month"), rs.getInt("year"));
                budget.setCategoryName(rs.getString("name"));
                return budget;
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error finding budget by id", e);
        }
        return null;
    }

    @Override
    public List<Budget> findByUserId(int userId) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT b.*, c.name FROM budgets b LEFT JOIN categories c ON b.category_id = c.id WHERE b.user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Budget budget = new Budget(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("category_id"),
                        rs.getDouble("limit_amount"), rs.getInt("month"), rs.getInt("year"));
                budget.setCategoryName(rs.getString("name"));
                budgets.add(budget);
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error finding budgets by user id", e);
        }
        return budgets;
    }

    @Override
    public List<Budget> findByUserIdAndMonth(int userId, int month, int year) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT b.*, c.name FROM budgets b LEFT JOIN categories c ON b.category_id = c.id WHERE b.user_id = ? AND b.month = ? AND b.year = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Budget budget = new Budget(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("category_id"),
                        rs.getDouble("limit_amount"), rs.getInt("month"), rs.getInt("year"));
                budget.setCategoryName(rs.getString("name"));
                budgets.add(budget);
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error finding budgets by user id and month", e);
        }
        return budgets;
    }

    @Override
    public void update(Budget budget) {
        String sql = "UPDATE budgets SET limit_amount = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, budget.getLimitAmount());
            stmt.setInt(2, budget.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.logError("Error updating budget", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM budgets WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.logError("Error deleting budget", e);
        }
    }
}
