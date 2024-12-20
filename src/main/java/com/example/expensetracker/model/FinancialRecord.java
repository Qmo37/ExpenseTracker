package com.example.expensetracker.model;

import java.time.LocalDateTime;

public class FinancialRecord {
    private final LocalDateTime dateTime;
    private final TransactionType type;
    private final String category;
    private final double amount;

    public enum TransactionType {
        REVENUE, EXPENSE
    }

    public FinancialRecord(String dateTime, TransactionType type, String category, double amount) {
        this.dateTime = dateTime;
        this.type = type;
        this.category = category;
        this.amount = amount;
    }

    // Getters
    public LocalDateTime getDateTime() { return dateTime; }
    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
}
