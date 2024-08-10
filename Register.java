package org.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Register extends Application {

    static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("register.css")).toExternalForm());
        stage.setScene(scene);
        Image icon = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("signup.png"))));
        stage.getIcons().add(icon);
        stage.setTitle("SignUp-Page-LMS");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
