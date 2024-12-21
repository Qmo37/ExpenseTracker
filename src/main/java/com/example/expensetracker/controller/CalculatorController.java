package com.example.expensetracker.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

/**
 * A controller class for handling the calculator functionality in the expense tracker application.
 * This class manages user input, performs calculations, and updates the display.
 */
public class CalculatorController {
    private final StringProperty displayValue = new SimpleStringProperty("0"); // The current display value as a StringProperty, allowing data binding with UI components.
    private final StringBuilder inputBuffer = new StringBuilder(); // Buffer to store user input (e.g., numbers and operators).
    // Flags and variables for managing the state of calculations.
    private boolean isNewCalculation = true;
    private double lastNumber = 0;
    private String lastOperator = "";
    // UI components for the calculator.
    private GridPane calculator;
    private Label displayLabel;

    //Initializes the calculator with its UI components.
    public void initialize(GridPane calculator, Label displayLabel) {
        this.calculator = calculator;
        this.displayLabel = displayLabel;
        setupCalculator();
    }

    // Returns the display value as a StringProperty.
    public StringProperty displayValueProperty() {
        return displayValue;
    }

    // Sets up the calculator buttons and their event handlers.
    public void setupCalculator() {
        if (calculator == null) {
            throw new IllegalStateException("Calculator GridPane not initialized");
        }
        // Defines the layout of the calculator buttons in rows and columns.
        String[][] buttons = {
                {"CE", "C", "%", "÷"},
                {"7", "8", "9", "×"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"±", "0", ".", "="}
        };
        // Iterates over the button layout and assigns event handlers.
        for (String[] row : buttons) {
            for (String buttonText : row) {
                Button button = findCalculatorButton(buttonText);
                if (button != null) {
                    button.setOnAction(e -> handleCalculatorButton(buttonText));
                }
            }
        }
    }
    // Finds a calculator button by its text value.
    private Button findCalculatorButton(String text) {
        if (calculator == null) return null;

        for (javafx.scene.Node node : calculator.getChildren()) {
            if (node instanceof Button button && button.getText().equals(text)) {
                return button;
            }
        }
        return null;
    }

    // Handles the user input from the calculator buttons.
    private void handleCalculatorButton(String value) {
        switch (value) {
            case "C", "CE" -> clear();
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

    // Handles the input of a number.
    private void handleNumber(String number) {
        if (isNewCalculation) {
            inputBuffer.setLength(0);
            isNewCalculation = false;
        }
        inputBuffer.append(number);
        displayValue.set(inputBuffer.toString());
    }

    // Handles the input of an operator (+, -, *, /).
    private void handleOperator(String operator) {
        if (!inputBuffer.isEmpty()) {
            lastNumber = Double.parseDouble(inputBuffer.toString());
            lastOperator = operator;
            inputBuffer.setLength(0);
            isNewCalculation = true;
        }
    }
    // Calculates the result based on the last operator and the current input.
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
            displayValue.set(String.format("%.2f", result));
            inputBuffer.setLength(0);
            inputBuffer.append(displayValue.get());
            lastOperator = "";
            isNewCalculation = true;
        }
    }

    // Clears the calculator input and resets the state.
    public void clear() {
        inputBuffer.setLength(0);
        lastNumber = 0;
        lastOperator = "";
        displayValue.set("0");
        isNewCalculation = true;
    }

    // Negates the current value (e.g., changes sign from positive to negative).
    private void negateCurrentValue() {
        if (!inputBuffer.isEmpty()) {
            double value = Double.parseDouble(inputBuffer.toString());
            value = -value;
            inputBuffer.setLength(0);
            inputBuffer.append(value);
            displayValue.set(inputBuffer.toString());
        }
    }

    // Adds a decimal point to the current value.
    private void addDecimalPoint() {
        if (isNewCalculation) {
            inputBuffer.setLength(0);
            isNewCalculation = false;
        }
        if (inputBuffer.length() == 0) {
            inputBuffer.append("0");
        }
        if (!inputBuffer.toString().contains(".")) {
            inputBuffer.append(".");
            displayValue.set(inputBuffer.toString());
        }
    }

    //Updates the calculator display with the current value.
    private void updateDisplay() {
        if (displayLabel != null) {
            displayLabel.setText(displayValue.get());
        }
    }

    // Checks if the current value is not zero and not empty.
    public boolean hasValue() {
        return !displayValue.get().equals("0") && !displayValue.get().isEmpty();
    }

    // Returns the current value as a String.
    public String getCurrentValue() {
        return displayValue.get();
    }
}
