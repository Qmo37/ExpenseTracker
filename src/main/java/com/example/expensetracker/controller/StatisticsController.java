package com.example.expensetracker.controller;

import com.example.expensetracker.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import java.time.LocalDate;
import java.util.Map;

public class StatisticsController {
    @FXML private PieChart expensePieChart;
    @FXML private PieChart revenuePieChart;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> categoryFilterBox;
    @FXML private Label totalExpenseLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private TableView<FinancialRecord> filteredTable;
    @FXML private Button refreshButton;
    @FXML private Button closeButton;

    private final FinancialService financialService;

    public StatisticsController(FinancialService financialService) {
        this.financialService = financialService;
    }

    @FXML
    public void initialize() {
        setupDatePickers();
        setupCategoryFilter();
        setupTable();
        updateCharts();
    }

    private void setupDatePickers() {
        // Default to last 30 days
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(30));

        // Add listeners for date changes
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateCharts());
        endDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateCharts());
    }

    private void setupCategoryFilter() {
        // Combine expense and revenue categories
        categoryFilterBox.getItems().add("All Categories");
        for (ExpenseCategory category : ExpenseCategory.values()) {
            categoryFilterBox.getItems().add(category.getDisplayName());
        }
        for (RevenueCategory category : RevenueCategory.values()) {
            categoryFilterBox.getItems().add(category.getDisplayName());
        }

        categoryFilterBox.setValue("All Categories");
        categoryFilterBox.setOnAction(e -> updateCharts());
    }

    private void setupTable() {
        // Date Column
        TableColumn<FinancialRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateTime().toLocalDate().toString()
        ));

        // Type Column
        TableColumn<FinancialRecord, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getType().toString()
        ));

        // Category Column
        TableColumn<FinancialRecord, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCategory()
        ));

        // Amount Column
        TableColumn<FinancialRecord, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.format("$%.2f", cellData.getValue().getAmount())
        ));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        // Set column styles
        dateCol.setStyle("-fx-alignment: CENTER-LEFT;");
        typeCol.setStyle("-fx-alignment: CENTER;");
        categoryCol.setStyle("-fx-alignment: CENTER-LEFT;");

        filteredTable.getColumns().setAll(dateCol, typeCol, categoryCol, amountCol);
    }

    private void updateCharts() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String selectedCategory = categoryFilterBox.getValue();

        FinancialSummary summary = financialService.generateSummary(startDate, endDate);

        // Update pie charts
        updateExpensePieChart(summary.getExpenseCategoryTotals());
        updateRevenuePieChart(summary.getRevenueCategoryTotals());

        // Update labels
        totalExpenseLabel.setText(String.format("Total Expenses: $%.2f", summary.getTotalExpense()));
        totalRevenueLabel.setText(String.format("Total Revenue: $%.2f", summary.getTotalRevenue()));

        // Update table with filtered data
        updateFilteredTable(startDate, endDate, selectedCategory);
    }

    private void updateExpensePieChart(Map<String, Double> expenseTotals) {
        expensePieChart.setData(FXCollections.observableArrayList());

        // Define colors for different categories
        String[] colors = {
                "#FF6B6B", "#4ECB71", "#45AAF2", "#F7C137", "#A55EEA",
                "#FF9F43", "#2D98DA", "#26DE81", "#EB3B5A", "#4B7BEC"
        };

        int colorIndex = 0;
        for (Map.Entry<String, Double> entry : expenseTotals.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            expensePieChart.getData().add(slice);

            // Apply custom colors
            slice.getNode().setStyle(
                    "-fx-pie-color: " + colors[colorIndex % colors.length] + ";"
            );
            colorIndex++;
        }

        // Add hover effect for pie slices
        int finalColorIndex = colorIndex;
        int finalColorIndex1 = colorIndex;
        expensePieChart.getData().forEach(data -> {
            data.getNode().setOnMouseEntered(event -> {
                data.getNode().setStyle(data.getNode().getStyle() +
                        "-fx-pie-color: derive(" + colors[finalColorIndex % colors.length] + ", 30%);"
                );
            });
            data.getNode().setOnMouseExited(event -> {
                data.getNode().setStyle(data.getNode().getStyle() +
                        "-fx-pie-color: " + colors[finalColorIndex1 % colors.length] + ";"
                );
            });
        });
    }

    private void updateRevenuePieChart(Map<String, Double> revenueTotals) {
        revenuePieChart.setData(FXCollections.observableArrayList());

        // Define colors for different categories
        String[] colors = {
                "#FF6B6B", "#4ECB71", "#45AAF2", "#F7C137", "#A55EEA",
                "#FF9F43", "#2D98DA", "#26DE81", "#EB3B5A", "#4B7BEC"
        };

        int colorIndex = 0;
        for (Map.Entry<String, Double> entry : revenueTotals.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            revenuePieChart.getData().add(slice);

            // Apply custom colors
            slice.getNode().setStyle(
                    "-fx-pie-color: " + colors[colorIndex % colors.length] + ";"
            );
            colorIndex++;
        }

        // Add hover effect for pie slices
        int finalColorIndex = colorIndex;
        int finalColorIndex1 = colorIndex;
        revenuePieChart.getData().forEach(data -> {
            data.getNode().setOnMouseEntered(event -> {
                data.getNode().setStyle(data.getNode().getStyle() +
                        "-fx-pie-color: derive(" + colors[finalColorIndex % colors.length] + ", 30%);"
                );
            });
            data.getNode().setOnMouseExited(event -> {
                data.getNode().setStyle(data.getNode().getStyle() +
                        "-fx-pie-color: " + colors[finalColorIndex1 % colors.length] + ";"
                );
            });
        });
    }

    private void updateFilteredTable(LocalDate startDate, LocalDate endDate, String category) {
        filteredTable.setItems(financialService.getFilteredRecords(startDate, endDate)
                .filtered(record ->
                        category.equals("All Categories") || record.getCategory().equals(category)
                ));
    }

    private void setupCloseButton() {
        closeButton.setOnAction(e -> closeWindow());
    }

    @FXML
    private void closeWindow() {
        closeButton.getScene().getWindow().hide();
    }

    private void setupRefreshButton() {
        refreshButton.setOnAction(e -> refresh());
    }

    @FXML
    private void refresh() {
        updateCharts();
    }
}