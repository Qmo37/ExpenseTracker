package com.example.expensetracker.controller;

import com.example.expensetracker.model.*;
import com.example.expensetracker.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import java.util.EnumSet;

public class ExpenseTrackerController {
    // Controllers
    private final CalculatorController calculatorController;
    private final TableViewController tableViewController;
    private final FinancialService financialService;

    // Properties
    private final DoubleProperty budget = new SimpleDoubleProperty(6000.0);
    private String currentCategory = null;
    private FinancialRecord.TransactionType currentType = FinancialRecord.TransactionType.EXPENSE;

    // FXML injected controls
    @FXML private Label displayLabel;
    @FXML private DatePicker datePicker;
    @FXML private GridPane calculator;
    @FXML private TableView<FinancialRecord> historyTable;
    @FXML private Label budgetLabel;
    @FXML private Label spendingLabel;
    @FXML private Button enterButton;
    @FXML private VBox categorySection;
    @FXML private ComboBox<FinancialRecord.TransactionType> typeComboBox;



    private final List<Button> categoryButtons = new ArrayList<>();

    public ExpenseTrackerController() {
        this.calculatorController = new CalculatorController();
        this.tableViewController = new TableViewController();
        this.financialService = new FinancialService();
    }

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDateTime.now().toLocalDate());

        // Setup calculator
        calculatorController.setupCalculator(calculator);
        displayLabel.textProperty().bind(calculatorController.displayValueProperty());

        // Setup transaction type combo box
        typeComboBox.setItems(FXCollections.observableArrayList(FinancialRecord.TransactionType.values()));
        typeComboBox.setValue(FinancialRecord.TransactionType.EXPENSE);
        typeComboBox.setOnAction(e -> updateCategoryButtons());

        // Setup table
        tableViewController.initialize(historyTable);

        setupCategoryButtons();
        setupEnterButton();
        updateDisplays();

        // Bind displays to financial service
        financialService.totalExpenseProperty().addListener((obs, old, newValue) ->
                updateDisplays());
    }

    private void setupCategoryButtons() {
        categorySection.getChildren().clear();
        categoryButtons.clear();

        // Get appropriate categories based on transaction type
        String[] categories = (currentType == FinancialRecord.TransactionType.EXPENSE) ?
                getExpenseCategories() : getRevenueCategories();

        for (String category : categories) {
            Button button = new Button(category);
            button.getStyleClass().add("category-button");
            button.setOnAction(e -> selectCategory(category, button));
            categoryButtons.add(button);
            categorySection.getChildren().add(button);
        }
    }

    private String[] getExpenseCategories() {
        return EnumSet.allOf(ExpenseCategory.class).stream()
                .map(ExpenseCategory::getDisplayName)
                .toArray(String[]::new);
    }

    private String[] getRevenueCategories() {
        return EnumSet.allOf(RevenueCategory.class).stream()
                .map(RevenueCategory::getDisplayName)
                .toArray(String[]::new);
    }

    private void updateCategoryButtons() {
        currentType = typeComboBox.getValue();
        currentCategory = null;
        setupCategoryButtons();
    }

    private void selectCategory(String category, Button button) {
        categoryButtons.forEach(b -> b.getStyleClass().remove("selected"));
        button.getStyleClass().add("selected");
        currentCategory = category;
    }

    private void setupEnterButton() {
        enterButton.setOnAction(e -> recordTransaction());
    }

    private void recordTransaction() {
        if (currentCategory == null || !calculatorController.hasValue()) {
            showAlert("Error", "Please select a category and enter an amount");
            return;
        }

        try {
            double amount = Double.parseDouble(calculatorController.getCurrentValue());
            LocalDateTime dateTime = datePicker.getValue().atTime(
                    LocalDateTime.now().getHour(),
                    LocalDateTime.now().getMinute()
            );

            FinancialRecord record = new FinancialRecord(
                    DateTimeUtil.formatDateTime(dateTime).get(),
                    currentType,
                    currentCategory,
                    amount
            );

            financialService.addRecord(record);
            tableViewController.addRecord(record);

            updateDisplays();
            calculatorController.clear();
            clearCategorySelection();

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount");
        }
    }

    private void updateDisplays() {
        LocalDate startDate = datePicker.getValue().minusDays(30);
        LocalDate endDate = datePicker.getValue(); // 日期選擇器的值

        FinancialSummary summary = financialService.generateSummary(startDate, endDate);
        budgetLabel.setText(String.format("Budget: %s", CurrencyUtil.formatCurrency(budget.get())));
        spendingLabel.setText(String.format("Spending: %s", CurrencyUtil.formatCurrency(summary.getTotalExpense())));
    }


    private void clearCategorySelection() {
        categoryButtons.forEach(b -> b.getStyleClass().remove("selected"));
        currentCategory = null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}