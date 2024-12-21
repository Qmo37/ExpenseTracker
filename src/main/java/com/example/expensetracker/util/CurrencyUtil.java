package com.example.expensetracker.util;
import javafx.beans.property.SimpleStringProperty;
import java.text.NumberFormat;
import java.util.Locale;

// A utility class for formatting currency values.
public class CurrencyUtil {
    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.US);

    // Formats the given amount as a currency string.
    public static String formatCurrency(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }
}
