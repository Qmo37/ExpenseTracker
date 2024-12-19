package com.example.expensetracker.util;

import javafx.beans.property.SimpleStringProperty;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {
    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.US);

    public static String formatCurrency(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }


}
