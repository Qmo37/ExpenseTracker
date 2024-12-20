package com.example.expensetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/expensetracker/main-scene.fxml")); // 這裡的路徑要改
            Scene scene = new Scene(fxmlLoader.load());

            String cssResource = getClass().getResource("/com/example/expensetracker/styles.css").toExternalForm();
            scene.getStylesheets().add(cssResource);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);


    }
}
