package com.budgettracker.dao;

import com.budgettracker.model.SavingGoal;
import com.budgettracker.util.DatabaseUtil;
import com.budgettracker.util.LoggerUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SavingGoalDaoImpl implements SavingGoalDao {

    @Override
    public void create(SavingGoal goal) {
        String sql = "INSERT INTO saving_goals (user_id, goal_name, target_amount, current_amount, deadline, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, goal.getUserId());
            stmt.setString(2, goal.getGoalName());
            stmt.setDouble(3, goal.getTargetAmount());
            stmt.setDouble(4, goal.getCurrentAmount());
            stmt.setDate(5, goal.getDeadline() != null ? Date.valueOf(goal.getDeadline()) : null);
            stmt.setString(6, goal.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                goal.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error creating saving goal", e);
        }
    }

    @Override
    public SavingGoal findById(int id) {
        String sql = "SELECT * FROM saving_goals WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToSavingGoal(rs);
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error finding saving goal by id", e);
        }
        return null;
    }

    @Override
    public List<SavingGoal> findByUserId(int userId) {
        List<SavingGoal> goals = new ArrayList<>();
        String sql = "SELECT * FROM saving_goals WHERE user_id = ? ORDER BY deadline ASC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                goals.add(mapResultSetToSavingGoal(rs));
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error finding saving goals by user id", e);
        }
        return goals;
    }

    @Override
    public void update(SavingGoal goal) {
        String sql = "UPDATE saving_goals SET goal_name = ?, target_amount = ?, current_amount = ?, deadline = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, goal.getGoalName());
            stmt.setDouble(2, goal.getTargetAmount());
            stmt.setDouble(3, goal.getCurrentAmount());
            stmt.setDate(4, goal.getDeadline() != null ? Date.valueOf(goal.getDeadline()) : null);
            stmt.setString(5, goal.getStatus());
            stmt.setInt(6, goal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.logError("Error updating saving goal", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM saving_goals WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LoggerUtil.logError("Error deleting saving goal", e);
        }
    }

    private SavingGoal mapResultSetToSavingGoal(ResultSet rs) throws SQLException {
        SavingGoal goal = new SavingGoal();
        goal.setId(rs.getInt("id"));
        goal.setUserId(rs.getInt("user_id"));
        goal.setGoalName(rs.getString("goal_name"));
        goal.setTargetAmount(rs.getDouble("target_amount"));
        goal.setCurrentAmount(rs.getDouble("current_amount"));
        Date deadline = rs.getDate("deadline");
        if (deadline != null) {
            goal.setDeadline(deadline.toLocalDate());
        }
        goal.setStatus(rs.getString("status"));
        return goal;
    }
}
