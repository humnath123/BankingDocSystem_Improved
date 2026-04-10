package com.budgettracker.dao;

import com.budgettracker.model.User;
import java.util.List;

public interface UserDao {
    void create(User user);
    User findById(int id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    void update(User user);
    void delete(int id);
}
