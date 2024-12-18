module com.example.expensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.expensetracker to javafx.fxml;
    exports com.example.expensetracker;
}