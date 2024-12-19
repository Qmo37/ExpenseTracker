package com.example.expensetracker.model;

import java.util.HashMap;
import java.util.Map;

public class FinancialSummary {
    private final Map<String, Double> revenueCategoryTotals;
    private final Map<String, Double> expenseCategoryTotals;
    private final double totalRevenue;
    private final double totalExpense;

    public FinancialSummary(
            Map<String, Double> revenueCategoryTotals,
            Map<String, Double> expenseCategoryTotals,
            double totalRevenue,
            double totalExpense) {
        this.revenueCategoryTotals = new HashMap<>(revenueCategoryTotals);
        this.expenseCategoryTotals = new HashMap<>(expenseCategoryTotals);
        this.totalRevenue = totalRevenue;
        this.totalExpense = totalExpense;
    }

    // Getters
    public Map<String, Double> getRevenueCategoryTotals() {
        return new HashMap<>(revenueCategoryTotals);
    }

    public Map<String, Double> getExpenseCategoryTotals() {
        return new HashMap<>(expenseCategoryTotals);
    }

    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalExpense() { return totalExpense; }
    public double getRemainingAmount() { return totalRevenue - totalExpense; }
}
