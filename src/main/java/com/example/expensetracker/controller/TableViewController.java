package com.example.expensetracker.controller;

import com.example.expensetracker.model.FinancialRecord;
import com.example.expensetracker.util.CurrencyUtil;
import com.example.expensetracker.util.DateTimeUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableViewController {
    public void initializeTable(TableView<FinancialRecord> tableView, ObservableList<FinancialRecord> records) {
        setupColumns(tableView);
        tableView.setItems(records);
    }

    private void setupColumns(TableView<FinancialRecord> tableView) {
        TableColumn<FinancialRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data ->
                DateTimeUtil.formatDateTime(data.getValue().getDateTime()));

        TableColumn<FinancialRecord, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory()));

        TableColumn<FinancialRecord, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(data ->
                CurrencyUtil.formatCurrency(data.getValue().getAmount()));

        tableView.getColumns().setAll(dateColumn, categoryColumn, amountColumn);
    }
}