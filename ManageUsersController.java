/**
 * @author MuhammadHasnatRasool
 * date: 11/8/2024
 * ManageUsers controller for LMS
 */

// package declaration

package org.librarymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Vector;

import static org.librarymanagementsystem.UtilityMethods.isEmpty;

public class ManageUsersController implements Initializable {

    @FXML
    private Label backLbl;

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<Integer, Integer> IdColumn;
    @FXML
    private TableColumn<User, String> NameColumn;
    @FXML
    private TableColumn<User , String> contactColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TextField  nameField,emailField,passwordField,contactField;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));


        loadUsersTableData();
        handleRow();
    }

    @FXML
    private void moveBack() throws IOException {
        UtilityMethods methods = new UtilityMethods();
        methods.moveBack(backLbl);
    }

    @FXML
    private void close() {
        UtilityMethods.close(backLbl);
    }

    private void loadUsersTableData() {
        String query = "SELECT * FROM users";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            Vector<User> records = new Vector<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String contact = rs.getString("contact");

                records.add(new User(id, name, email, password,contact));
            }
            ObservableList<User> userList = FXCollections.observableArrayList(records);
            userTableView.setItems(userList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addButtonActionPerformed() {
        if (validateSignUp()) {
            if (!checkDuplicateUser()) {
                if (addEmployee()) {
                    showAlert(Alert.AlertType.INFORMATION, "employee inserted successfully!", "Employee-Insertion");
                    loadUsersTableData();
                } else {
                    showAlert(Alert.AlertType.WARNING, "employee insertion failed!", "Employee-Insertion");
                }
            }
            else {
                showAlert(Alert.AlertType.WARNING, "Username already exists","ManageEmployees");
            }
        }
    }
    private boolean addEmployee() {
        String query = "INSERT INTO users (name, email, password, contact) VALUES (?, ?, ?, ?)";
        return executeUpdate(query, nameField.getText(),emailField.getText(), passwordField.getText(),contactField.getText());
    }

    private boolean checkDuplicateUser() {
        String name = nameField.getText();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE name = ?")) {

            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {

        }
        return false;
    }

    private boolean validateSignUp() {
        if (isEmpty(nameField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Username is missing!","Missing Data");
            return false;
        }

        if (checkDuplicateUser()) {
            showAlert(Alert.AlertType.WARNING, "Username already exists","Duplicate-Username");
            return false;
        }

        if (isEmpty(passwordField.getText()) || passwordField.getText().length() < 3) {
            showAlert(Alert.AlertType.WARNING, "Password can't be empty","Invalid Password");
            return false;
        }

        if (isEmpty(emailField.getText()) || !emailField.getText().contains("@")) {
            showAlert(Alert.AlertType.WARNING, "Please enter a valid email","Invalid Email");
            return false;
        }

        if (isEmpty(contactField.getText())) {
            showAlert(Alert.AlertType.WARNING, "Please enter a contact number","Missing Data");
            return false;
        }

        return true;
    }


    @FXML
    private void updateButtonActionPerformed() {
        if (updateEmployee()) {
            showAlert(Alert.AlertType.INFORMATION, "Employee updated successfully!", "Employee-Update");
            loadUsersTableData();
        } else {
            showAlert(Alert.AlertType.WARNING, "Employee update failed!", "Employee-Update");
        }
    }


    private boolean updateEmployee() {
        String query = "UPDATE users  email = ?, password = ?, contact = ? WHERE name = ?";
        return executeUpdate(query, emailField.getText(), passwordField.getText(),contactField.getText() , nameField.getText());
    }

    @FXML
    private void deleteButtonActionPerformed() {
        if (deleteEmployee()) {
            showAlert(Alert.AlertType.INFORMATION, "Employee deleted successfully!", "Employee-Deletion");
            loadUsersTableData();
        } else {
            showAlert(Alert.AlertType.WARNING, "Employee deletion failed!", "Employee-Deletion");
        }
    }

    private boolean deleteEmployee() {
        String query = "DELETE FROM users WHERE name = ?";
        return executeUpdate(query, nameField.getText());
    }

    private boolean executeUpdate(String query, Object... params) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param instanceof Integer) {
                    pst.setInt(i + 1, (Integer) param);
                } else if (param instanceof String) {
                    pst.setString(i + 1, (String) param);
                }

            }

            int rowCount = pst.executeUpdate();
            return rowCount > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String content, String header) {
        Alert alert = new Alert(type);
        alert.setTitle("Manage-Employees");
        alert.setContentText(content);
        alert.setHeaderText(header);
        alert.initOwner(backLbl.getScene().getWindow());
        alert.show();
    }

    private void handleRow() {
        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                User user  = (User) newSelection;
                nameField.setText(user.getName());
                emailField.setText(user.getEmail());
                passwordField.setText(user.getPassword());
                contactField.setText(user.getContact());
            }
        });
    }

    @FXML
    private void clear() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        contactField.setText("");
    }
}

