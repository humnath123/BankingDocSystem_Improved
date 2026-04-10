package com.budgettracker.model;

public class Budget {
    private int id;
    private int userId;
    private int categoryId;
    private double limitAmount;
    private int month;
    private int year;
    private String categoryName;

    public Budget() {}

    public Budget(int id, int userId, int categoryId, double limitAmount, int month, int year) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.limitAmount = limitAmount;
        this.month = month;
        this.year = year;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getLimitAmount() { return limitAmount; }
    public void setLimitAmount(double limitAmount) { this.limitAmount = limitAmount; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    @Override
    public String toString() {
        return "Budget{" + "categoryName='" + categoryName + "', limitAmount=" + limitAmount + '}';
    }
}
