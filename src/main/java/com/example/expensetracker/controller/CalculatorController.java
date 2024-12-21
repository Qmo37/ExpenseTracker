package com.example.expensetracker.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.Node;

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

    private static final String PRESSED_STYLE = "-fx-background-color: #4A4458;"; // Darker color when pressed
    private static final String NORMAL_STYLE = "-fx-background-color: #403D49;"; // Your normal button color

    //Initializes the calculator with its UI components.
    public void initialize(GridPane calculator, Label displayLabel) {
        this.calculator = calculator;
        this.displayLabel = displayLabel;
        setupCalculator();
        setupKeyboardInput();
    }

    private void setupKeyboardInput() {
        // Add key event handler to the calculator grid after it's created
        Platform.runLater(() -> {
            Scene scene = calculator.getScene();
            scene.setOnKeyPressed(this::handleKeyboardInput);
        });
    }

    private void handleKeyboardInput(KeyEvent event) {
        String key = event.getText();
        KeyCode code = event.getCode();

        if (event.isConsumed()) return;

        Button targetButton = null;

        switch (code) {
            case DIGIT0, NUMPAD0 -> targetButton = findCalculatorButton("0");
            case DIGIT1, NUMPAD1 -> targetButton = findCalculatorButton("1");
            case DIGIT2, NUMPAD2 -> targetButton = findCalculatorButton("2");
            case DIGIT3, NUMPAD3 -> targetButton = findCalculatorButton("3");
            case DIGIT4, NUMPAD4 -> targetButton = findCalculatorButton("4");
            case DIGIT5, NUMPAD5 -> targetButton = findCalculatorButton("5");
            case DIGIT6, NUMPAD6 -> targetButton = findCalculatorButton("6");
            case DIGIT7, NUMPAD7 -> targetButton = findCalculatorButton("7");
            case DIGIT8, NUMPAD8 -> targetButton = findCalculatorButton("8");
            case DIGIT9, NUMPAD9 -> targetButton = findCalculatorButton("9");
            case PERIOD, DECIMAL -> targetButton = findCalculatorButton(".");
            case PLUS, ADD -> targetButton = findCalculatorButton("+");
            case MINUS, SUBTRACT -> targetButton = findCalculatorButton("-");
            case MULTIPLY -> targetButton = findCalculatorButton("×");
            case SLASH, DIVIDE -> targetButton = findCalculatorButton("÷");
            case EQUALS, ENTER -> targetButton = findCalculatorButton("=");
            case BACK_SPACE, DELETE -> targetButton = findCalculatorButton("C");
            case ESCAPE -> targetButton = findCalculatorButton("CE");
            default -> {
                if (key.equals("%")) {
                    targetButton = findCalculatorButton("%");
                }
            }
        }

        if (targetButton != null) {
            pressButton(targetButton);
        }

        event.consume();
    }

    private void pressButton(Button button) {
        // Store original style
        String originalStyle = button.getStyle();

        // Apply pressed style
        button.setStyle(originalStyle + ";" + PRESSED_STYLE);

        // Handle the calculation
        handleCalculatorButton(button.getText());

        // Create a timeline for the visual feedback
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(button.styleProperty(),
                        originalStyle + ";" + PRESSED_STYLE)),
                new KeyFrame(Duration.millis(100), new KeyValue(button.styleProperty(),
                        originalStyle))
        );

        timeline.play();
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
                    // Set initial style
                    button.setStyle(NORMAL_STYLE + "; -fx-background-radius: 1000;");

                    // Setup mouse press handlers
                    button.setOnMousePressed(e -> {
                        button.setStyle(button.getStyle() + ";" + PRESSED_STYLE);
                    });

                    button.setOnMouseReleased(e -> {
                        button.setStyle(NORMAL_STYLE + "; -fx-background-radius: 1000;");
                        handleCalculatorButton(buttonText);
                    });

                    // Handle mouse exit while pressed
                    button.setOnMouseExited(e -> {
                        button.setStyle(NORMAL_STYLE + "; -fx-background-radius: 1000;");
                    });
                }
            }
        }
    }
    // Finds a calculator button by its text value.
    private Button findCalculatorButton(String text) {
        if (calculator == null) return null;

        for (Node node : calculator.getChildren()) {
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
