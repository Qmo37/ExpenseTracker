package com.example.expensetracker.model;

public enum ExpenseCategory {
    FOOD("Food"),
    TRANSPORT("Transport"),
    SHOPPING("Shopping"),
    COFFEE("Coffee"),
    GIFT("Gift");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
