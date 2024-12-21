package com.example.expensetracker.model;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

// A service class for managing financial records and calculations in the expense tracker application.
public class FinancialService {
    private final ObservableList<FinancialRecord> records = FXCollections.observableArrayList();
    private final DoubleProperty totalExpense = new SimpleDoubleProperty(0.0);
    private final DoubleProperty totalRevenue = new SimpleDoubleProperty(0.0);

    // Initializes the financial service with default values.
    public void addRecord(FinancialRecord record) {
        records.add(record);
        if (record.getType() == FinancialRecord.TransactionType.EXPENSE) {
            totalExpense.set(totalExpense.get() + record.getAmount());
        }else if (record.getType() == FinancialRecord.TransactionType.REVENUE) {
            totalRevenue.set(totalRevenue.get() + record.getAmount());
        }
    }

    // Adds a financial record to the list of records.
    public ObservableList<FinancialRecord> getRecords() {
        return records;
    }

    // Returns the list of financial records.
    public FinancialSummary generateSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> revenueTotals = new HashMap<>();
        Map<String, Double> expenseTotals = new HashMap<>();
        double totalRevenue = 0;
        double totalExpense = 0;

        // Calculate totals for revenue and expenses within the specified date range
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

    // Generates a financial summary for the specified date range.
    public ObservableList<FinancialRecord> getFilteredRecords(LocalDate startDate, LocalDate endDate) {
        return records.stream()
                .filter(record -> {
                    LocalDate recordDate = record.getDateTime().toLocalDate();
                    return !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate);
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    // Returns a list of financial records filtered by the specified date range.
    public DoubleProperty totalExpenseProperty() {
        return totalExpense;
    }

    // Returns the total expense as a DoubleProperty.
    public DoubleProperty totalRevenueProperty() {
        return totalRevenue;
    }

    public DoubleBinding totalBalanceProperty() { return totalRevenue.subtract(totalExpense); }
}
