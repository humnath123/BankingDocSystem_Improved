package com.budgettracker.dao;

import com.budgettracker.model.SavingGoal;
import java.util.List;

public interface SavingGoalDao {
    void create(SavingGoal goal);
    SavingGoal findById(int id);
    List<SavingGoal> findByUserId(int userId);
    void update(SavingGoal goal);
    void delete(int id);
}
