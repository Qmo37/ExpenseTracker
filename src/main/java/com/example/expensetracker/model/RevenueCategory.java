package com.example.expensetracker.model;

// An enumeration of revenue categories for financial records.
public enum RevenueCategory {
    SALARY("Salary", "fas-wallet"),
    INVESTMENT("Investment", "fas-search-dollar"),
    BONUS("Bonus", "fas-award"),
    INTEREST("Interest", "fas-piggy-bank"),
    OTHER("Other", "fas-plus-circle");

    private final String displayName;
    private final String iconName;

    RevenueCategory(String displayName, String iconName) {
        this.displayName = displayName;
        this.iconName = iconName;
    }

    //Getters
    public String getDisplayName() { return displayName; }
    public String getIconName() { return iconName; }
}
