package com.example.expensetracker.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FinancialService {
    private final ObservableList<FinancialRecord> records = FXCollections.observableArrayList();
    private double budget = 6000.0; // Default budget

    public void addRecord(FinancialRecord record) {
        records.add(record);
    }

    public ObservableList<FinancialRecord> getRecords() {
        return records;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getBudget() {
        return budget;
    }

    public FinancialSummary generateSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> revenueTotals = new HashMap<>();
        Map<String, Double> expenseTotals = new HashMap<>();
        double totalRevenue = 0;
        double totalExpense = 0;

        for (FinancialRecord record : records) {
            LocalDate recordDate = record.getDateTime().toLocalDate();
            if (!recordDate.isBefore(startDate) && !recordDate.isAfter(endDate)) {
                if (record.getType() == FinancialRecord.TransactionType.REVENUE) {
                    revenueTotals.merge(record.getCategory(), record.getAmount(), Double::sum);
                    totalRevenue += record.getAmount();
                } else {
                    expenseTotals.merge(record.getCategory(), record.getAmount(), Double::sum);
                    totalExpense += record.getAmount();
                }
            }
        }

        return new FinancialSummary(revenueTotals, expenseTotals, totalRevenue, totalExpense);
    }

    public ObservableList<FinancialRecord> getFilteredRecords(LocalDate startDate, LocalDate endDate) {
        return records.stream()
                .filter(record -> {
                    LocalDate recordDate = record.getDateTime().toLocalDate();
                    return !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate);
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public void addExpense(LocalDate date, String category, double amount) {
        // Implementation of the addExpense method
    }

    public double getTotalSpending() {
        // Implementation of the getTotalSpending method
        return 0.0; // Replace with actual implementation
    }
}
