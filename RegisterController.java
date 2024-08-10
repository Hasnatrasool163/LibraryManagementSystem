package org.librarymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    private TextField nameField, emailField, contactField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @FXML
    public void signUp() throws SQLException, IOException {
        if (validateSignUp()) {
            if (!checkDuplicateUser()) {
                insertSignUpDetails();
            } else {
                showAlert(Alert.AlertType.WARNING, "Username already exists");
            }
        }
    }

    private void insertSignUpDetails() throws SQLException, IOException {
        try (PreparedStatement pst = getPreparedStatement()) {
            int updateRowCount = pst.executeUpdate();
            if (updateRowCount > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Record Inserted Successfully");
                clearFields();
                switchToLoginPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Record Insertion Failure");
            }
        }
    }

    private PreparedStatement getPreparedStatement() throws SQLException {
        Connection con = DBConnection.getConnection();
        String sql = "INSERT INTO users (name, password, email, contact) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, nameField.getText());
        pst.setString(2, passwordField.getText());
        pst.setString(3, emailField.getText());
        pst.setString(4, contactField.getText());
        return pst;
    }

    private boolean validateSignUp() {
        if (isEmpty(nameField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Username is missing!");
            return false;
        }

        if (checkDuplicateUser()) {
            showAlert(Alert.AlertType.WARNING, "Username already exists");
            return false;
        }

        if (isEmpty(passwordField.getText()) || passwordField.getText().length() < 3) {
            showAlert(Alert.AlertType.WARNING, "Password can't be empty");
            return false;
        }

        if (isEmpty(emailField.getText()) || !emailField.getText().contains("@")) {
            showAlert(Alert.AlertType.WARNING, "Please enter a valid email");
            return false;
        }

//        if (!isValidEmail(email)) {
//            showAlert(Alert.AlertType.WARNING, "Please enter a valid email");
//            return false;
//        }

        if (isEmpty(contactField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Please enter a contact number");
            return false;
        }

        return true;
    }

    private boolean checkDuplicateUser() {
        String name = nameField.getText();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE name = ?")) {

            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    private void clearFields() {
        nameField.clear();
        passwordField.clear();
        emailField.clear();
        contactField.clear();
    }
    public void switchToLoginPage() throws IOException {

        Stage currentStage = (Stage) loginBtn.getScene().getWindow();
        currentStage.hide();


        Stage newStage = new Stage();


        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Scene scene = new Scene(root);


        newStage.setScene(scene);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("login.png"))));
        newStage.setTitle("Login-Page-LMS");
        newStage.setResizable(false);
        newStage.setAlwaysOnTop(true);


        newStage.show();
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }



    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(loginBtn.getScene().getWindow());
        alert.showAndWait();
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

}
