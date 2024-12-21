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