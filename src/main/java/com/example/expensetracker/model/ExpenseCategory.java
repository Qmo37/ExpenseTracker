package com.example.expensetracker.model;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

// An enumeration of expense categories for financial records.
public enum ExpenseCategory {
    FOOD("Food", "fas-utensils"),              // 🍴 Utensils
    TRANSPORT("Transport", "fas-car"),         // 🚗 Car
    SHOPPING("Shopping", "fas-shopping-bag"),  // 🛍️ Shopping bag
    COFFEE("Coffee", "fas-coffee"),           // ☕ Coffee cup
    GIFT("Gift", "fas-gift"),                 // 🎁 Gift box
    MEDICAL("Medical", "fas-stethoscope"),    // 👨‍⚕️ Medical
    BILL("Bill", "fas-file-invoice"),         // 📄 Bill/Invoice
    EDUCATION("Education", "fas-graduation-cap"), // 🎓 Education
    ENTERTAINMENT("Entertainment", "fas-gamepad"), // 🎮 Entertainment
    GROCERY("Grocery", "fas-shopping-cart");   // 🛒 Shopping cart

    private final String displayName;
    private final String iconName;

    ExpenseCategory(String displayName, String iconName) {
        this.displayName = displayName;
        this.iconName = iconName;
    }

    // Getters
    public String getDisplayName() { return displayName; }
    public String getIconName() { return iconName; }
}
