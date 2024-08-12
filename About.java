package org.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class About extends Application {

    static Stage stage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage= new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("about.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Image icon = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("about.png"))));
        stage.getIcons().add(icon);
        stage.setTitle("AboutPage-LMS");
        stage.setResizable(false);
        stage.show();
    }

}
