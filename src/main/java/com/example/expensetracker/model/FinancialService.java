package com.example.expensetracker.model;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class FinancialService {
    private final ObservableList<FinancialRecord> records = FXCollections.observableArrayList();
    private final DoubleProperty totalExpense = new SimpleDoubleProperty(0.0);
    private final DoubleProperty totalRevenue = new SimpleDoubleProperty(0.0);

    public void addRecord(FinancialRecord record) {
        records.add(record);
        if (record.getType() == FinancialRecord.TransactionType.EXPENSE) {
            totalExpense.set(totalExpense.get() + record.getAmount());
        }else if (record.getType() == FinancialRecord.TransactionType.REVENUE) {
            totalRevenue.set(totalRevenue.get() + record.getAmount());
        }
    }

    public ObservableList<FinancialRecord> getRecords() {
        return records;
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

    public DoubleProperty totalExpenseProperty() {
        return totalExpense;
    }

    public DoubleProperty totalRevenueProperty() {
        return totalRevenue;
    }

    public DoubleBinding totalBalanceProperty() { return totalRevenue.subtract(totalExpense); }
}
