package com.example.expensetracker.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

public class CalculatorController {
    private final StringProperty displayValue = new SimpleStringProperty("0");
    private final StringBuilder inputBuffer = new StringBuilder();
    private boolean isNewCalculation = true;
    private double lastNumber = 0;
    private String lastOperator = "";
    private GridPane calculator;
    private Label displayLabel;

    public void initialize(GridPane calculator, Label displayLabel) {
        this.calculator = calculator;
        this.displayLabel = displayLabel;
        setupCalculator();
    }

    public StringProperty displayValueProperty() {
        return displayValue;
    }

    public void setupCalculator() {
        if (calculator == null) {
            throw new IllegalStateException("Calculator GridPane not initialized");
        }

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

    private Button findCalculatorButton(String text) {
        if (calculator == null) return null;

        for (javafx.scene.Node node : calculator.getChildren()) {
            if (node instanceof Button button && button.getText().equals(text)) {
                return button;
            }
        }
        return null;
    }

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

    private void handleNumber(String number) {
        if (isNewCalculation) {
            inputBuffer.setLength(0);
            isNewCalculation = false;
        }
        inputBuffer.append(number);
        displayValue.set(inputBuffer.toString());
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
            displayValue.set(String.format("%.2f", result));
            inputBuffer.setLength(0);
            inputBuffer.append(displayValue.get());
            lastOperator = "";
            isNewCalculation = true;
        }
    }

    public void clear() {
        inputBuffer.setLength(0);
        lastNumber = 0;
        lastOperator = "";
        displayValue.set("0");
        isNewCalculation = true;
    }

    private void negateCurrentValue() {
        if (!inputBuffer.isEmpty()) {
            double value = Double.parseDouble(inputBuffer.toString());
            value = -value;
            inputBuffer.setLength(0);
            inputBuffer.append(value);
            displayValue.set(inputBuffer.toString());
        }
    }

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

    private void updateDisplay() {
        if (displayLabel != null) {
            displayLabel.setText(displayValue.get());
        }
    }

    public boolean hasValue() {
        return !displayValue.get().equals("0") && !displayValue.get().isEmpty();
    }

    public String getCurrentValue() {
        return displayValue.get();
    }
}
