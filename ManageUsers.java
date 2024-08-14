package org.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ManageUsers extends Application {

    static Stage stage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage= new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("manageusers.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Image icon = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("user.png"))));
        stage.getIcons().add(icon);
        stage.setTitle("ManageUsers-Page-LMS");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
