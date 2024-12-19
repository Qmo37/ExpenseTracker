package com.example.expensetracker.controller;

import com.example.expensetracker.model.FinancialRecord;
import com.example.expensetracker.util.CurrencyUtil;
import com.example.expensetracker.util.DateTimeUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableViewController {
    public void initializeTable(TableView<FinancialRecord> tableView, ObservableList<FinancialRecord> records) {
        setupColumns(tableView);
        tableView.setItems(records);
    }

    private void setupColumns(TableView<FinancialRecord> tableView) {
        // 日期欄位
        TableColumn<FinancialRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data ->
                DateTimeUtil.formatDateTime(data.getValue().getDateTime())); // 已經回傳 ObservableValue<String>

        // 類別欄位
        TableColumn<FinancialRecord, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory()));

        // 金額欄位
        TableColumn<FinancialRecord, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(data ->
                new SimpleStringProperty(CurrencyUtil.formatCurrency(data.getValue().getAmount()))); // 將 String 包裝成 ObservableValue<String>

        tableView.getColumns().setAll(dateColumn, categoryColumn, amountColumn);
    }
}
