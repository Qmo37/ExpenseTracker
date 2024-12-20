module com.example.expensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.expensetracker to javafx.fxml;
    opens com.example.expensetracker.controller to javafx.fxml;
    opens com.example.expensetracker.model to javafx.base;
    opens com.example.expensetracker.util to javafx.base;
    exports com.example.expensetracker;
}