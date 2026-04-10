package com.budgettracker.model;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private double amount;
    private TransactionType type;
    private Category category;
    private LocalDate date;
    private String note;
    private User user;

    public Transaction() {}

    public Transaction(int id, double amount, TransactionType type, Category category, LocalDate date, String note, User user) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.note = note;
        this.user = user;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", amount=" + amount + ", type=" + type + ", category=" + (category != null ? category.getName() : "N/A") + ", date=" + date + ", note='" + note + "'}";
    }
}
