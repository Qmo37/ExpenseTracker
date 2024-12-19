package com.example.expensetracker.controller;

import com.example.expensetracker.model.*;
import com.example.expensetracker.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class ExpenseTrackerController {
    private final FinancialService financialService;
    private final CalculatorController calculatorController;
    private final TableViewController tableViewController;

    @FXML private TableView<FinancialRecord> historyTable;
    @FXML private Label displayLabel;
    @FXML private DatePicker datePicker;
    @FXML private Label budgetLabel;
    @FXML private Label spendingLabel;
    @FXML private GridPane calculator;
    @FXML private VBox categorySection;
    @FXML private Button enterButton;
    @FXML private ChoiceBox<String> typeChoiceBox;

    public ExpenseTrackerController() {
        this.financialService = new FinancialService();
        this.calculatorController = new CalculatorController();
        this.tableViewController = new TableViewController();
    }

    @FXML
    public void initialize() {
        setupBindings();
        setupTableView();
        setupCalculator();
        setupCategoryButtons();
        setupTypeChoiceBox();
        setupDatePicker();
    }

    private void setupBindings() {
        displayLabel.textProperty().bind(calculatorController.displayValueProperty());
        budgetLabel.setText(CurrencyUtil.formatCurrency(financialService.getBudget()));
        tableViewController.initializeTable(historyTable, financialService.getRecords());
    }

    private void setupTableView() {
        tableViewController.initializeTable(historyTable, financialService.getRecords());
    }

    private void setupCalculator() {
        calculatorController.initialize(calculator);
    }

    private void setupCategoryButtons() {
        categorySection.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setOnAction(event -> {
                    String category = button.getText();
                    double amount = Double.parseDouble(displayLabel.getText());
                    financialService.addExpense(LocalDate.now(), category, amount);
                    updateSpending();
                });
            }
        });
    }

    private void setupTypeChoiceBox() {
        typeChoiceBox.getItems().addAll("Expense", "Income");
        typeChoiceBox.setValue("Expense");
    }

    private void setupDatePicker() {
        datePicker.setValue(LocalDate.now());
    }

    private void updateSpending() {

        spendingLabel.setText(CurrencyUtil.formatCurrency(financialService.getTotalSpending()));


    }

}