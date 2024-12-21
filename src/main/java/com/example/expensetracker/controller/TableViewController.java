package com.example.expensetracker.controller;
import com.example.expensetracker.model.FinancialRecord;
import com.example.expensetracker.util.CurrencyUtil;
import com.example.expensetracker.util.DateTimeUtil;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

// A controller class for managing the TableView of financial records in the expense tracker application.
public class TableViewController {
    private TableView<FinancialRecord> tableView;
    private final ObservableList<FinancialRecord> records = FXCollections.observableArrayList();

    // Initializes the TableView with the provided TableView instance.
    public void initialize(TableView<FinancialRecord> tableView) {
        this.tableView = tableView;
        setupColumns();
        tableView.setItems(records);
    }

    // Sets up the columns of the TableView.
    private void setupColumns() {
        TableColumn<FinancialRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data -> DateTimeUtil.formatDateTime(data.getValue().getDateTime()));

        TableColumn<FinancialRecord, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getType().toString()));

        TableColumn<FinancialRecord, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory()));

        TableColumn<FinancialRecord, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(data ->
                new SimpleStringProperty(CurrencyUtil.formatCurrency(data.getValue().getAmount())));

        tableView.getColumns().setAll(dateColumn, typeColumn, categoryColumn, amountColumn);
    }

    // Adds a financial record to the TableView.
    public void addRecord(FinancialRecord record) {
        records.add(0, record);
    }

    // Clears all financial records from the TableView.
    public void clearRecords() {
        records.clear();
    }

    // Returns the list of financial records in the TableView.
    public ObservableList<FinancialRecord> getRecords() {
        return records;
    }
}
