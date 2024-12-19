package com.example.expensetracker.model;

public enum RevenueCategory {
    SALARY("Salary"),
    INVESTMENT("Investment"),
    BONUS("Bonus"),
    INTEREST("Interest"),
    OTHER("Other");

    private final String displayName;

    RevenueCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
