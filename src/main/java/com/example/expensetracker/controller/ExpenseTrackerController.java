package com.example.expensetracker.controller;

import com.example.expensetracker.model.*;
import com.example.expensetracker.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
        budgetLabel.textProperty().bind(CurrencyUtil.formatCurrency(financialService.getBudget()));
        tableViewController.initializeTable(historyTable, financialService.getRecords());
    }

    // ... (other methods as in previous implementation)
}
