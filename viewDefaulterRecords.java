package org.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class viewDefaulterRecords extends Application {

    static Stage stage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        stage= new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("viewdefaulterrecords.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Image icon = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("student.png"))));
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("viewDefaulterRecord-Page-LMS");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
