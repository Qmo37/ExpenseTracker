package com.example.expensetracker.controller;

import com.example.expensetracker.Main;
import com.example.expensetracker.model.*;
import com.example.expensetracker.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;

public class ExpenseTrackerController extends Main {
    // Controllers
    private final CalculatorController calculatorController;
    private final TableViewController tableViewController;
    private final FinancialService financialService;

    // Properties
    public ScrollPane categoryScrollPane;
    private String currentCategory = null;
    private FinancialRecord.TransactionType currentType = FinancialRecord.TransactionType.EXPENSE;

    // FXML injected controls
    @FXML private Label displayLabel;
    @FXML private DatePicker datePicker;
    @FXML private GridPane calculator;
    @FXML private TableView<FinancialRecord> historyTable;
    @FXML private Label revenueLabel;
    @FXML private Label expenseLabel;
    @FXML private Label revenueAmount;
    @FXML private Label expenseAmount;
    @FXML private Label balanceLabel;
    @FXML private Label balanceAmount;
    @FXML private Button enterButton;
    @FXML private VBox categorySection;
    @FXML private ComboBox<String> typeComboBox; // Changed to String type
    @FXML private Button statisticsButton;

    private final List<VBox> categoryButtons = new ArrayList<VBox>();

    // Constructor to initialize controllers
    public ExpenseTrackerController() {
        this.calculatorController = new CalculatorController();
        this.tableViewController = new TableViewController();
        this.financialService = new FinancialService();
    }

    // Main initialization method called after FXML loading
    @FXML
    public void initialize() {
        initializeDatePicker();
        initializeCalculator();
        initializeTypeComboBox();
        initializeTable();
        initializeCategoryButtons();
        initializeEnterButton();
        updateDisplays();
        initializeAmountLabels();

        // Bind displays to financial service dynamically
        if (financialService != null) {
            financialService.totalExpenseProperty().addListener((obs, old, newValue) ->
                    updateDisplays());
        }
    }
    // Initialize the date picker to the current date
    private void initializeDatePicker() {
        if (datePicker != null) {
            datePicker.setValue(LocalDateTime.now().toLocalDate());
        }
    }
    // Initialize the calculator and bind its display to the label
    private void initializeCalculator() {
        if (calculator != null && displayLabel != null) {
            calculatorController.initialize(calculator, displayLabel);
            displayLabel.textProperty().bind(calculatorController.displayValueProperty());
        }
    }
    // Set up the transaction type combobox with "Expense" and "Revenue" options
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

    // Initialize the transaction history table
    private void initializeTable() {
        if (historyTable != null) {
            tableViewController.initialize(historyTable);
        }
    }

    // Initialize category buttons based on the selected transaction type
    private void initializeCategoryButtons() {
        if (categorySection != null) {
            setupCategoryButtons();
        }
    }

    // Set up the enter button to record transactions
    private void initializeEnterButton() {
        if (enterButton != null) {
            enterButton.setOnAction(e -> recordTransaction());
        }
    }

    // Bind revenue and expense labels to properties in the financial service
    private void initializeAmountLabels() {
        if (revenueAmount != null && expenseAmount != null && balanceAmount != null) {
            revenueAmount.textProperty().bind(financialService.totalRevenueProperty().asString("$%.2f"));
            expenseAmount.textProperty().bind(financialService.totalExpenseProperty().asString("$%.2f"));
            balanceAmount.textProperty().bind(financialService.totalBalanceProperty().asString("$%.2f"));
        }
    }

    // Set up category buttons dynamically based on the transaction type
    private void setupCategoryButtons() {
        categorySection.getChildren().clear();
        categoryButtons.clear();

        // Get appropriate categories based on transaction type
        if (currentType == FinancialRecord.TransactionType.EXPENSE) {
            for (ExpenseCategory category : ExpenseCategory.values()) {
                createCategoryButton(category.getDisplayName(), category.getIconName());
            }
        } else {
            for (RevenueCategory category : RevenueCategory.values()) {
                createCategoryButton(category.getDisplayName(), category.getIconName());
            }
        }
    }

    private void createCategoryButton(String category, String iconName) {
        VBox buttonContainer = new VBox(5);
        buttonContainer.setAlignment(Pos.CENTER);

        // Create icon
        FontIcon icon = new FontIcon(iconName);
        icon.setIconSize(24);
        icon.setIconColor(Color.WHITE);

        // Create label
        Label label = new Label(category);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 10px;");

        // Add to container
        buttonContainer.getChildren().addAll(icon, label);
        buttonContainer.getStyleClass().add("category-button");

        // Add click handler
        buttonContainer.setOnMouseClicked(e -> selectCategory(category, buttonContainer));

        categoryButtons.add(buttonContainer);
        categorySection.getChildren().add(buttonContainer);
    }

    private void updateCategoryButtons() {
        if (categorySection != null) {
            setupCategoryButtons();
            clearCategorySelection();
        }
    }

    // Select a specific category and mark it visually
    private void selectCategory(String category, VBox buttonContainer) {
        // Clear previous selection
        categoryButtons.forEach(b -> b.getStyleClass().remove("selected"));
        // Set new selection
        buttonContainer.getStyleClass().add("selected");
        currentCategory = category;
    }

    // Record a transaction with the current inputs
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

    // Update revenue and expense displays based on the selected date
    private void updateDisplays() {
        if (revenueAmount != null && expenseAmount != null) {
            LocalDate startDate = datePicker.getValue().minusDays(30);
            LocalDate endDate = datePicker.getValue();

//            FinancialSummary summary = financialService.generateSummary(startDate, endDate);
//            revenueAmount.setText(String.format("%s", CurrencyUtil.formatCurrency(summary.getTotalRevenue())));
//            expenseAmount.setText(String.format("%s", CurrencyUtil.formatCurrency(summary.getTotalExpense())));
        }
    }

    // Clear the current category selection
    private void clearCategorySelection() {
        categoryButtons.forEach(b -> b.getStyleClass().remove("selected"));
        currentCategory = null;
    }

    // Show an error alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Set up the statistics button to open a new window
    private void setupStatisticsButton() {
        statisticsButton.setOnAction(e -> showStatisticsWindow());
    }

    // Show the statistics window
    @FXML
    private void showStatisticsWindow() {
        try {
            // Use Main class to load FXML
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/expensetracker/statistics-view.fxml"));

            // Create and set controller
            StatisticsController statisticsController = new StatisticsController(financialService);
            loader.setController(statisticsController);

            // Create new stage and scene
            Stage statisticsStage = new Stage();
            Scene scene = new Scene(loader.load());

            // Add stylesheet to match main window
            scene.getStylesheets().add(Main.class.getResource("/com/example/expensetracker/styles.css").toExternalForm());

            // Configure stage
            statisticsStage.initStyle(StageStyle.UNDECORATED);  // Match main window style
            statisticsStage.setTitle("Statistics");
            statisticsStage.setScene(scene);
            statisticsStage.initModality(Modality.WINDOW_MODAL);
            statisticsStage.initOwner(calculator.getScene().getWindow());

            statisticsStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not load statistics window: " + ex.getMessage());
        }
    }
}