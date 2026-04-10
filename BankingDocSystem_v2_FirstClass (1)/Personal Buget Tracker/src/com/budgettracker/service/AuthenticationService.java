package com.budgettracker.service;

import com.budgettracker.dao.UserDao;
import com.budgettracker.dao.UserDaoImpl;
import com.budgettracker.model.User;

public class AuthenticationService {
    private UserDao userDao = new UserDaoImpl();

    public User authenticate(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) { // In real app, hash password
            return user;
        }
        return null;
    }

    public void register(User user) {
        userDao.create(user);
    }
}
