package com.example.expensetracker.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CalculatorController {
    private final StringProperty displayValue = new SimpleStringProperty("0");
    private final StringBuilder inputBuffer = new StringBuilder();
    private boolean isNewCalculation = true;
    private double lastNumber = 0;
    private String lastOperator = "";

    public StringProperty displayValueProperty() {
        return displayValue;
    }

    public void handleInput(String value) {
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

    private void clear() {
        inputBuffer.setLength(0);
        lastNumber = 0;
        lastOperator = "";
        displayValue.set("0");
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



    // ... (calculator methods as in previous implementation)
}
