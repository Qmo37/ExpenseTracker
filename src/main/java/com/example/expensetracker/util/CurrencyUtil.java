package com.example.expensetracker.util;

import javafx.beans.property.SimpleStringProperty;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {
    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.US);

    public static SimpleStringProperty formatCurrency(double amount) {
        return new SimpleStringProperty(CURRENCY_FORMAT.format(amount));
    }
}
