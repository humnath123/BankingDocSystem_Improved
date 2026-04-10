package com.budgettracker.service;

import com.budgettracker.dao.SavingGoalDaoImpl;
import com.budgettracker.model.SavingGoal;
import java.util.List;

public class SavingGoalService {
    private SavingGoalDaoImpl goalDao = new SavingGoalDaoImpl();

    public void createGoal(SavingGoal goal) {
        if (validateGoal(goal)) {
            goalDao.create(goal);
        }
    }

    public void updateGoal(SavingGoal goal) {
        if (validateGoal(goal)) {
            goalDao.update(goal);
        }
    }

    public void deleteGoal(int goalId) {
        goalDao.delete(goalId);
    }

    public SavingGoal getGoal(int goalId) {
        return goalDao.findById(goalId);
    }

    public List<SavingGoal> getUserGoals(int userId) {
        return goalDao.findByUserId(userId);
    }

    public void addSavingAmount(SavingGoal goal, double amount) {
        if (amount > 0) {
            goal.setCurrentAmount(goal.getCurrentAmount() + amount);
            if (goal.getCurrentAmount() >= goal.getTargetAmount()) {
                goal.setStatus("COMPLETED");
            }
            goalDao.update(goal);
        }
    }

    public boolean isGoalCompleted(SavingGoal goal) {
        return goal.getCurrentAmount() >= goal.getTargetAmount();
    }

    private boolean validateGoal(SavingGoal goal) {
        return goal.getTargetAmount() > 0 && goal.getUserId() > 0 && goal.getGoalName() != null && !goal.getGoalName().isEmpty();
    }
}
