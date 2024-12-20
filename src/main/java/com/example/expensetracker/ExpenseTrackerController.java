package com.example.expensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;

public class ExpenseTrackerController {
    // Model
    private List<Expense> expenses = new ArrayList<>();
    private DoubleProperty budget = new SimpleDoubleProperty(6000.0);
    private DoubleProperty totalSpending = new SimpleDoubleProperty(0.0);
    private StringProperty currentCalculatorValue = new SimpleStringProperty("0");
    private String currentCategory = null;
    private StringBuilder inputBuffer = new StringBuilder();
    private boolean isNewCalculation = true;

    // FXML injected controls
    @FXML private Label displayLabel;
    @FXML private DatePicker datePicker;
    @FXML private GridPane calculator;
    @FXML private VBox historyBox;
    @FXML private Label budgetLabel;
    @FXML private Label spendingLabel;
    @FXML private Button enterButton;
    @FXML private VBox categorySection;


    // Category buttons
    private List<Button> categoryButtons = new ArrayList<>();
    private double lastNumber = 0;
    private String lastOperator = "";

    @FXML
    public void initialize() {
        // Initialize date picker
        datePicker.setValue(LocalDate.now());

        // Bind calculator display
        displayLabel.textProperty().bind(currentCalculatorValue);

        // Initialize budget display
        updateBudgetDisplay();

        // Setup calculator buttons
        setupCalculator();

        // Setup category buttons
        setupCategoryButtons();

        // Setup enter button
        setupEnterButton();


    }

    private void setupCalculator() {
        String[][] buttons = {
                {"CE", "C", "%", "÷"},
                {"7", "8", "9", "×"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"±", "0", ".", "="}
        };

        for (String[] row : buttons) {
            for (String buttonText : row) {
                Button button = findCalculatorButton(buttonText);
                if (button != null) {
                    button.setOnAction(e -> handleCalculatorButton(buttonText));
                }
            }
        }
    }

    private void handleCalculatorButton(String value) {
        switch (value) {
            case "C", "CE" -> clearCalculator();
            case "+" -> handleOperator("+");
            case "-" -> handleOperator("-");
            case "×" -> handleOperator("*");
            case "÷" -> handleOperator("/");
            case "=" -> calculateResult();
            case "±" -> negateCurrentValue();
            case "." -> addDecimalPoint();
            default -> handleNumber(value);
        }
        updateDisplay();
    }

    private void handleNumber(String number) {
        if (isNewCalculation) {
            inputBuffer.setLength(0);
            isNewCalculation = false;
        }
        inputBuffer.append(number);
        currentCalculatorValue.set(inputBuffer.toString());
    }

    private void handleOperator(String operator) {
        if (!inputBuffer.isEmpty()) {
            lastNumber = Double.parseDouble(inputBuffer.toString());
            lastOperator = operator;
            inputBuffer.setLength(0);
            isNewCalculation = true;
        }
    }

    private void calculateResult() {
        if (!inputBuffer.isEmpty() && !lastOperator.isEmpty()) {
            double currentNumber = Double.parseDouble(inputBuffer.toString());
            double result = switch (lastOperator) {
                case "+" -> lastNumber + currentNumber;
                case "-" -> lastNumber - currentNumber;
                case "*" -> lastNumber * currentNumber;
                case "/" -> lastNumber / currentNumber;
                default -> currentNumber;
            };
            currentCalculatorValue.set(String.format("%.2f", result));
            inputBuffer = new StringBuilder(currentCalculatorValue.get());
            lastOperator = "";
            isNewCalculation = true;
        }
    }

    private void clearCalculator() {
        inputBuffer.setLength(0);
        lastNumber = 0;
        lastOperator = "";
        currentCalculatorValue.set("0");
        isNewCalculation = true;
    }

    private void negateCurrentValue() {
        if (!inputBuffer.isEmpty()) {
            double current = Double.parseDouble(inputBuffer.toString());
            currentCalculatorValue.set(String.format("%.2f", -current));
            inputBuffer = new StringBuilder(currentCalculatorValue.get());
        }
    }

    private void addDecimalPoint() {
        if (!inputBuffer.toString().contains(".")) {
            inputBuffer.append(".");
            currentCalculatorValue.set(inputBuffer.toString());
        }
    }

    private void setupCategoryButtons() {
        String[] categories = {"Shopping", "Gift", "Food", "Coffee", "Transport"};
        for (String category : categories) {
            Button button = new Button(category);
            button.getStyleClass().add("category-button");
            button.setOnAction(e -> selectCategory(category, button));
            categoryButtons.add(button);
            categorySection.getChildren().add(button);
        }
    }

    private void selectCategory(String category, Button button) {
        // Clear previous selection
        categoryButtons.forEach(b -> b.getStyleClass().remove("selected"));
        // Set new selection
        button.getStyleClass().add("selected");
        currentCategory = category;
    }

    private void setupEnterButton() {
        enterButton.setOnAction(e -> recordExpense());
    }

    private void recordExpense() {
        if (currentCategory == null || inputBuffer.isEmpty()) {
            showAlert("Error", "Please select a category and enter an amount");
            return;
        }

        try {
            double amount = Double.parseDouble(currentCalculatorValue.get());
            LocalDateTime dateTime = datePicker.getValue().atTime(LocalDateTime.now().getHour(),
                    LocalDateTime.now().getMinute());

            Expense expense = new Expense(dateTime, currentCategory, amount);
            expenses.add(expense);
            totalSpending.set(totalSpending.get() + amount);

            updateHistory();
            updateBudgetDisplay();
            clearCalculator();

            // Clear category selection
            categoryButtons.forEach(b -> b.getStyleClass().remove("selected"));
            currentCategory = null;

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount");
        }
    }

    private void updateHistory() {
        historyBox.getChildren().clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Expense expense : expenses) {
            Label historyItem = new Label(String.format("%s %s: $%.2f",
                    expense.dateTime.format(formatter),
                    expense.category,
                    expense.amount));
            historyItem.getStyleClass().add("history-item");
            historyBox.getChildren().add(0, historyItem); // Add at top
        }
    }

    private void updateBudgetDisplay() {
        budgetLabel.setText(String.format("Budget: $%.2f", budget.get()));
        spendingLabel.setText(String.format("Spending: $%.2f", totalSpending.get()));
    }

    private void updateDisplay() {
        displayLabel.setText(currentCalculatorValue.get());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Button findCalculatorButton(String text) {
        for (javafx.scene.Node node : calculator.getChildren()) {
            if (node instanceof Button button && button.getText().equals(text)) {
                return button;
            }
        }
        return null;
    }

    // Data model
    private static class Expense {
        private final LocalDateTime dateTime;
        private final String category;
        private final double amount;

        public Expense(LocalDateTime dateTime, String category, double amount) {
            this.dateTime = dateTime;
            this.category = category;
            this.amount = amount;
        }
    }
}