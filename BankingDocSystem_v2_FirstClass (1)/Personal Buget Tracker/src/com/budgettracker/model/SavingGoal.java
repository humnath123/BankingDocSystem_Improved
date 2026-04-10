package com.budgettracker.model;

import java.time.LocalDate;

public class SavingGoal {
    private int id;
    private int userId;
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private LocalDate deadline;
    private String status;

    public SavingGoal() {}

    public SavingGoal(int id, int userId, String goalName, double targetAmount, double currentAmount, LocalDate deadline, String status) {
        this.id = id;
        this.userId = userId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getProgressPercentage() {
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }

    @Override
    public String toString() {
        return "SavingGoal{" + "id=" + id + ", goalName='" + goalName + "', targetAmount=" + targetAmount + ", currentAmount=" + currentAmount + '}';
    }
}
