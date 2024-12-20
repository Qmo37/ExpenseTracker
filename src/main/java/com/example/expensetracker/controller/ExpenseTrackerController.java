package com.example.expensetracker.controller;

import com.example.expensetracker.model.*;
import com.example.expensetracker.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private ComboBox<String> typeComboBox; // Changed to String type

    private final List<Button> categoryButtons = new ArrayList<>();

    public ExpenseTrackerController() {
        this.calculatorController = new CalculatorController();
        this.tableViewController = new TableViewController();
        this.financialService = new FinancialService();
    }

    @FXML
    public void initialize() {
        initializeDatePicker();
        initializeCalculator();
        initializeTypeComboBox();
        initializeTable();
        initializeCategoryButtons();
        initializeEnterButton();
        updateDisplays();

        // Bind displays to financial service
        if (financialService != null) {
            financialService.totalExpenseProperty().addListener((obs, old, newValue) ->
                    updateDisplays());
        }
    }

    private void initializeDatePicker() {
        if (datePicker != null) {
            datePicker.setValue(LocalDateTime.now().toLocalDate());
        }
    }

    private void initializeCalculator() {
        if (calculator != null && displayLabel != null) {
            calculatorController.initialize(calculator, displayLabel);
            displayLabel.textProperty().bind(calculatorController.displayValueProperty());
        }
    }

    private void initializeTypeComboBox() {
        if (typeComboBox != null) {
            typeComboBox.setItems(FXCollections.observableArrayList("Expense", "Revenue"));
            typeComboBox.setValue("Expense");
            typeComboBox.setOnAction(e -> {
                currentType = typeComboBox.getValue().equals("Revenue") ?
                        FinancialRecord.TransactionType.REVENUE :
                        FinancialRecord.TransactionType.EXPENSE;
                updateCategoryButtons();
            });
        }
    }

    private void initializeTable() {
        if (historyTable != null) {
            tableViewController.initialize(historyTable);
        }
    }

    private void initializeCategoryButtons() {
        if (categorySection != null) {
            setupCategoryButtons();
        }
    }

    private void initializeEnterButton() {
        if (enterButton != null) {
            enterButton.setOnAction(e -> recordTransaction());
        }
    }

    private void setupCategoryButtons() {
        categorySection.getChildren().clear();
        categoryButtons.clear();

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
        if (categorySection != null) {
            setupCategoryButtons();
            clearCategorySelection();
        }
    }

    private void selectCategory(String category, Button button) {
        categoryButtons.forEach(b -> b.getStyleClass().remove("selected"));
        button.getStyleClass().add("selected");
        currentCategory = category;
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
                    dateTime,
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
        if (budgetLabel != null && spendingLabel != null) {
            LocalDate startDate = datePicker.getValue().minusDays(30);
            LocalDate endDate = datePicker.getValue();

            FinancialSummary summary = financialService.generateSummary(startDate, endDate);
            budgetLabel.setText(String.format("Budget: %s", CurrencyUtil.formatCurrency(budget.get())));
            spendingLabel.setText(String.format("Spending: %s", CurrencyUtil.formatCurrency(summary.getTotalExpense())));
        }
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