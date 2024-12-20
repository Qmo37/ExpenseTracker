package com.example.expensetracker.model;

public enum ExpenseCategory {
    FOOD("Food"),
    TRANSPORT("Transport"),
    SHOPPING("Shopping"),
    COFFEE("Coffee"),
    GIFT("Gift"),
    MEDICAL("Medical"),
    ENTERTAINMENT("Entertainment"),
    BILL("Bill"),
    EDUCATION("Education"),
    GROCERY("Grocery"),
    OTHERS("Others");


    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
