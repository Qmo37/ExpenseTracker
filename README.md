### Project Overview
The Expense Tracker is a JavaFX-based desktop application designed to help users manage their personal finances by tracking both expenses and revenues. It features a modern, intuitive user interface with a calculator, category management, and detailed financial analytics.
![GUI Concept](src/main/resources/com/example/expensetracker/Whole Window(2).png)
### Project Objectives
1. Provide an easy-to-use interface for recording financial transactions
2. Offer real-time calculation capabilities
3. Categorize expenses and revenues
4. Display financial statistics and trends
5. Maintain a transaction history
6. Generate visual reports of spending patterns

### Project Structure
```
ExpenseTracker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/expensetracker/
│   │   │       ├── controller/         # UI Controllers
│   │   │       ├── model/             # Data Models
│   │   │       ├── util/              # Utility Classes
│   │   │       └── Main.java          # Application Entry Point
│   │   └── resources/
│   │       └── com/example/expensetracker/
│   │           ├── fxml/              # FXML Layout Files
│   │           └── styles/            # CSS Stylesheets
└── pom.xml                            # Project Dependencies
```

### Key Components

1. **Controllers**:
   - `ExpenseTrackerController`: Main application controller
   - `CalculatorController`: Handles calculator functionality
   - `StatisticsController`: Manages statistics view
   - `TableViewController`: Controls transaction table display

2. **Models**:
   - `FinancialRecord`: Represents a financial transaction
   - `ExpenseCategory`: Enum of expense categories
   - `RevenueCategory`: Enum of revenue categories
   - `FinancialService`: Business logic for financial operations

3. **Utilities**:
   - `CurrencyUtil`: Currency formatting
   - `DateTimeUtil`: Date/time formatting
   ```java
   public class CurrencyUtil {
       // Currency formatting utility
       public static String formatCurrency(double amount)
   }
   
   public class DateTimeUtil {
       // Date/time formatting utility
       public static SimpleStringProperty formatDateTime(LocalDateTime dateTime)
   }
   ```

### Technologies & Dependencies

1. **Core Technologies**:
   - Java 21
   - JavaFX 23 (the latest version !!!)
   - FXML for UI layout
   - CSS for styling

2. **Dependencies** (from module-info.java):
   ```java
   requires javafx.controls;
   requires javafx.fxml;
   requires javafx.web;
   requires org.kordamp.ikonli.javafx;
   requires org.kordamp.bootstrapfx.core;
   requires org.kordamp.ikonli.fontawesome5;
   ```

### Key Features

1. **Calculator Integration**:
   - Built-in calculator for transaction amounts
   - Support for basic arithmetic operations
   - Keyboard input support

2. **Category Management**:
   - Pre-defined expense categories (Food, Transport, Shopping, etc.)
   - Pre-defined revenue categories (Salary, Investment, etc.)
   - Visual category selection with icons

3. **Financial Tracking**:
   - Real-time balance updates
   - Separate tracking of expenses and revenues
   - Transaction history with date, type, category, and amount

4. **Statistics and Visualization**:
   - Pie charts for expense and revenue distribution
   - Date range filtering
   - Category-based filtering
   - Summary statistics

### Design Patterns & Architecture

1. **MVC Pattern**:
   - Model: Financial data structures and business logic
   - View: FXML layouts and CSS styles
   - Controller: User interaction handling

2. **Observer Pattern**:
   - Using JavaFX properties for reactive updates
   - Binding UI elements to data models

3. **Factory Pattern**:
   - Creating UI components
   - Managing category instances

### User Interface

1. **Main Window**:
   - Calculator panel
   - Category selection
   - Transaction history
   - Financial summary

2. **Statistics Window**:
   - Pie charts for visualization
   - Filtered transaction table
   - Date range selection
   - Category filters

### Implementation Features

1. **Reactive Programming**
   ```java
   // Property binding for reactive updates
   private final DoubleProperty totalExpense = new SimpleDoubleProperty(0.0);
   private final DoubleProperty totalRevenue = new SimpleDoubleProperty(0.0);
   ```

2. **Event Handling**
   ```java
   // Calculator button handling
   private void handleCalculatorButton(String value) {
       switch (value) {
           case "C", "CE" -> clearCalculator();
           case "+" -> handleOperator("+");
           // ...
       }
   }
   ```

3. **Data Persistence**
   - In-memory storage
   - Transaction history maintenance
   - State management

4. **User Experience**
   - Modern dark theme
   - Responsive design
   - Intuitive navigation
   - Visual feedback
   - Error handling

### Styling
- Modern, dark theme
- Custom CSS for all components
- Responsive layout
- Animated transitions
- Icon integration for categories

This project demonstrates modern Java application development practices, combining functional programming concepts, reactive programming with JavaFX, and clean architecture principles to create a practical financial management tool.
### TODO List
- [x] Add a chart page to display financial data, e.g., pie chart, bar chart, or simply just search by date, etc.
- [x] Change overview panel from budget and spending to revenue, expense, and balance.
- [x] Change category selection from text to icons.
- [x] Optimize the CSS, especially for the table view.
- [x] Optimize the code, delete unnecessary code, and refactor the code.
- [x] Add comments to the code, especially for the methods.
- [ ] You tell me! Feel free to suggest any new features or improvements. (We might not be able to implement any of them.)

### Key features Claude said we can introduce in the presentation
Here are 8 key features of the Expense Tracker project explained in detail:

1. **Smart Calculator Integration**
```java
public class CalculatorController {
    private final StringProperty displayValue = new SimpleStringProperty("0");
    private final StringBuilder inputBuffer = new StringBuilder();
    private boolean isNewCalculation = true;
    private double lastNumber = 0;
    private String lastOperator = "";

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
    }
}
```
- Full arithmetic operations support
- Keyboard input integration
- Real-time display updates
- Error handling for invalid inputs
- Visual feedback for button presses
- Memory of previous calculations
- Decimal point handling
- Negative number support

2. **Dynamic Category Management**
```java
public enum ExpenseCategory {
    FOOD("Food", "fas-utensils"),
    TRANSPORT("Transport", "fas-car"),
    SHOPPING("Shopping", "fas-shopping-bag"),
    COFFEE("Coffee", "fas-coffee"),
    GIFT("Gift", "fas-gift"),
    // ...

    private final String displayName;
    private final String iconName;
}

private void setupCategoryButtons() {
    categorySection.getChildren().clear();
    categoryButtons.clear();

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
```
- Visual category representation
- Icon integration
- Dynamic category switching
- Type-based categorization (Expense/Revenue)
- Single selection mechanism
- Scrollable category list
- Visual feedback for selection
- Category-based filtering

3. **Financial Statistics and Visualization**
```java
public class StatisticsController {
    @FXML private PieChart expensePieChart;
    @FXML private PieChart revenuePieChart;
    
    private void updateCharts() {
        FinancialSummary summary = financialService.generateSummary(startDate, endDate);
        updateExpensePieChart(summary.getExpenseCategoryTotals());
        updateRevenuePieChart(summary.getRevenueCategoryTotals());
        
        totalExpenseLabel.setText(String.format("Total Expenses: $%.2f", 
            summary.getTotalExpense()));
        totalRevenueLabel.setText(String.format("Total Revenue: $%.2f", 
            summary.getTotalRevenue()));
    }
}
```
- Interactive pie charts
- Date range filtering
- Category distribution analysis
- Real-time updates
- Animated transitions
- Detailed tooltips
- Legend integration
- Color-coded categories

4. **Transaction Management System**
```java
public class FinancialService {
    private final ObservableList<FinancialRecord> records = FXCollections.observableArrayList();
    private final DoubleProperty totalExpense = new SimpleDoubleProperty(0.0);
    private final DoubleProperty totalRevenue = new SimpleDoubleProperty(0.0);

    public void addRecord(FinancialRecord record) {
        records.add(record);
        if (record.getType() == FinancialRecord.TransactionType.EXPENSE) {
            totalExpense.set(totalExpense.get() + record.getAmount());
        } else {
            totalRevenue.set(totalRevenue.get() + record.getAmount());
        }
    }
}
```
- Real-time transaction recording
- Automatic balance updates
- Transaction categorization
- Date-time tracking
- Type classification
- Amount validation
- Historical record keeping
- Observable collections for UI binding

5. **Reactive UI Updates**
```java
public class ExpenseTrackerController {
    @FXML private Label revenueAmount;
    @FXML private Label expenseAmount;
    @FXML private Label balanceAmount;

    private void initializeAmountLabels() {
        revenueAmount.textProperty().bind(
            financialService.totalRevenueProperty().asString("$%.2f"));
        expenseAmount.textProperty().bind(
            financialService.totalExpenseProperty().asString("$%.2f"));
        balanceAmount.textProperty().bind(
            financialService.totalBalanceProperty().asString("$%.2f"));
    }
}
```
- Property binding
- Automatic UI updates
- Format transformation
- Real-time calculations
- Currency formatting
- Color-coded displays
- Balance monitoring
- Zero-lag updates

6. **Advanced Filtering and Search**
```java
public ObservableList<FinancialRecord> getFilteredRecords(
    LocalDate startDate, LocalDate endDate, String category) {
    return records.stream()
        .filter(record -> {
            LocalDate recordDate = record.getDateTime().toLocalDate();
            boolean dateInRange = !recordDate.isBefore(startDate) 
                && !recordDate.isAfter(endDate);
            boolean categoryMatch = category.equals("All Categories") 
                || record.getCategory().equals(category);
            return dateInRange && categoryMatch;
        })
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
}
```
- Date range filtering
- Category filtering
- Dynamic results updating
- Multiple filter combinations
- Stream-based processing
- Observable results
- Efficient filtering
- Real-time updates

7. **Modern UI Design**
```css
.calculator-button {
    -fx-background-color: rgba(255,255,255,0.1);
    -fx-text-fill: white;
    -fx-min-width: 65px;
    -fx-min-height: 65px;
    -fx-font-size: 18px;
    -fx-background-radius: 1000px;
    -fx-transition: -fx-background-color 0.1s;
}

.category-button {
    -fx-background-color: #403D49;
    -fx-background-radius: 16;
    -fx-padding: 8;
    -fx-min-width: 50;
    -fx-min-height: 50;
    -fx-cursor: hand;
    -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);
}
```
- Dark theme implementation
- Responsive layout
- Custom animations
- Shadow effects
- Rounded corners
- Hover effects
- Color transitions
- Icon integration

8. **Data Summary and Analysis**
```java
public class FinancialSummary {
    private final Map<String, Double> revenueCategoryTotals;
    private final Map<String, Double> expenseCategoryTotals;
    private final double totalRevenue;
    private final double totalExpense;

    public double getRemainingAmount() { 
        return totalRevenue - totalExpense; 
    }

    public Map<String, Double> getExpenseCategoryTotals() {
        return new HashMap<>(expenseCategoryTotals);
    }
}
```
- Category-wise totals
- Period-based summaries
- Balance calculation
- Trend analysis
- Percentage distributions
- Category comparisons
- Historical tracking
- Data aggregation

Each of these features contributes to making the Expense Tracker a comprehensive financial management tool. The features are designed to work together seamlessly, providing users with both detailed tracking capabilities and high-level financial insights.