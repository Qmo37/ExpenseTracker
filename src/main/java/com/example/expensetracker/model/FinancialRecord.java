package com.example.expensetracker.model;

import java.time.LocalDateTime;

// A class representing a financial record with a date and time, transaction type, category, and amount.
public class FinancialRecord {
    private final LocalDateTime dateTime;
    private final TransactionType type;
    private final String category;
    private final double amount;

    // An enumeration of transaction types for financial records.
    public enum TransactionType {
        REVENUE, EXPENSE
    }

    public FinancialRecord(LocalDateTime dateTime, TransactionType type, String category, double amount) {
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
