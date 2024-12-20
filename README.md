### Project Structure
```
ExpenseTracker/
├── src/main/
│   ├── java/
│   │   └── com/example/expensetracker/
│   │       ├── controller/
│   │       │   ├── CalculatorController.java
│   │       │   ├── ExpenseTrackerController.java
│   │       │   └── TableViewController.java
│   │       ├── model/
│   │       │   ├── ExpenseCategory.java
│   │       │   ├── FinancialRecord.java
│   │       │   ├── FinancialService.java
│   │       │   ├── FinancialSummary.java
│   │       │   └── RevenueCategory.java
│   │       └── util/
│   │           ├── CurrencyUtil.java
│   │           └── DateTimeUtil.java
│   └── resources/
│       └── com/example/expensetracker/
│           ├── main-scene.fxml
│           └── styles.css
```

### Key Java Classes

1. **Controllers**:
    - `ExpenseTrackerController`: Main controller managing the application's logic
    - `CalculatorController`: Handles calculator functionality
    - `TableViewController`: Manages the history table display

2. **Models**:
    - `FinancialRecord`: Data model for financial transactions
    - `FinancialService`: Service layer managing financial operations
    - `FinancialSummary`: Summary of financial data
    - `ExpenseCategory` & `RevenueCategory`: Enums for transaction categories

3. **Utilities**:
    - `CurrencyUtil`: Currency formatting
    - `DateTimeUtil`: Date and time formatting

### Key Features

1. **Calculator Functionality**:
    - Basic arithmetic operations
    - Clear and reset functions
    - Decimal point handling
    - Negative number support

2. **Transaction Management**:
    - Support for both expenses and revenue
    - Category-based classification
    - Date selection for transactions
    - Transaction history tracking

3. **Financial Overview**:
    - Budget tracking
    - Total spending display
    - Historical transaction view
    - Category-based organization

4. **User Interface**:
    - Modern, dark-themed design
    - Interactive calculator
    - Category selection buttons
    - Tabular transaction history
    - Date picker
    - Transaction type selector (Expense/Revenue)

5. **Data Display**:
    - Formatted currency display
    - Organized transaction history table
    - Budget vs. spending comparison
    - Category-based transaction grouping

The application follows an MVC (Model-View-Controller) architecture pattern, with clear separation between:
- View (FXML and CSS files)
- Controllers (handling user interaction)
- Models (managing data and business logic)

The UI is built using JavaFX, with FXML for layout definition and CSS for styling. The application provides a comprehensive solution for personal finance tracking with both expense and revenue management capabilities.

### TODO List
- [x] Add a chart page to display financial data, e.g., pie chart, bar chart, or simply just search by date, etc.
- [x] Change overview panel from budget and spending to revenue, expense, and balance.
- [x] Change category selection from text to icons.
- [x] Optimize the CSS, especially for the table view.
- [x] Optimize the code, delete unnecessary code, and refactor the code.
- [x] Add comments to the code, especially for the methods.
- [x] You tell me! Feel free to suggest any new features or improvements. (We might not be able to implement any of them.)