package com.budgettracker.dao;

import com.budgettracker.model.*;
import com.budgettracker.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImpl implements TransactionDao {

    @Override
    public void create(Transaction transaction) {
        String sql = "INSERT INTO transactions (amount, type, category_id, date, note, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setString(2, transaction.getType().name());
            stmt.setInt(3, transaction.getCategory().getId());
            stmt.setDate(4, Date.valueOf(transaction.getDate()));
            stmt.setString(5, transaction.getNote());
            stmt.setInt(6, transaction.getUser().getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction findById(int id) {
        String sql = "SELECT t.*, c.name as category_name, u.id, u.username, u.password, u.email, u.phone, u.full_name, u.role FROM transactions t " +
                     "LEFT JOIN categories c ON t.category_id = c.id " +
                     "LEFT JOIN users u ON t.user_id = u.id WHERE t.id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User(rs.getInt("u.id"), rs.getString("username"), rs.getString("password"), 
                    rs.getString("email"), rs.getString("phone"), rs.getString("full_name"), rs.getString("role"));
                return new Transaction(rs.getInt("id"), rs.getDouble("amount"),
                        TransactionType.valueOf(rs.getString("type")), category,
                        rs.getDate("date").toLocalDate(), rs.getString("note"), user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> findByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, c.name as category_name, u.id, u.username, u.password, u.email, u.phone, u.full_name, u.role FROM transactions t " +
                     "LEFT JOIN categories c ON t.category_id = c.id " +
                     "LEFT JOIN users u ON t.user_id = u.id WHERE t.user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User(rs.getInt("u.id"), rs.getString("username"), rs.getString("password"), 
                    rs.getString("email"), rs.getString("phone"), rs.getString("full_name"), rs.getString("role"));
                transactions.add(new Transaction(rs.getInt("id"), rs.getDouble("amount"),
                        TransactionType.valueOf(rs.getString("type")), category,
                        rs.getDate("date").toLocalDate(), rs.getString("note"), user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByUserIdAndCategory(int userId, int categoryId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, c.name as category_name, u.id, u.username, u.password, u.email, u.phone, u.full_name, u.role FROM transactions t " +
                     "LEFT JOIN categories c ON t.category_id = c.id " +
                     "LEFT JOIN users u ON t.user_id = u.id WHERE t.user_id = ? AND t.category_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User(rs.getInt("u.id"), rs.getString("username"), rs.getString("password"), 
                    rs.getString("email"), rs.getString("phone"), rs.getString("full_name"), rs.getString("role"));
                transactions.add(new Transaction(rs.getInt("id"), rs.getDouble("amount"),
                        TransactionType.valueOf(rs.getString("type")), category,
                        rs.getDate("date").toLocalDate(), rs.getString("note"), user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByUserIdAndDateRange(int userId, Date startDate, Date endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, c.name as category_name, u.id, u.username, u.password, u.email, u.phone, u.full_name, u.role FROM transactions t " +
                     "LEFT JOIN categories c ON t.category_id = c.id " +
                     "LEFT JOIN users u ON t.user_id = u.id WHERE t.user_id = ? AND t.date BETWEEN ? AND ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User(rs.getInt("u.id"), rs.getString("username"), rs.getString("password"), 
                    rs.getString("email"), rs.getString("phone"), rs.getString("full_name"), rs.getString("role"));
                transactions.add(new Transaction(rs.getInt("id"), rs.getDouble("amount"),
                        TransactionType.valueOf(rs.getString("type")), category,
                        rs.getDate("date").toLocalDate(), rs.getString("note"), user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE transactions SET amount = ?, type = ?, category_id = ?, date = ?, note = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, transaction.getAmount());
            stmt.setString(2, transaction.getType().name());
            stmt.setInt(3, transaction.getCategory().getId());
            stmt.setDate(4, Date.valueOf(transaction.getDate()));
            stmt.setString(5, transaction.getNote());
            stmt.setInt(6, transaction.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
