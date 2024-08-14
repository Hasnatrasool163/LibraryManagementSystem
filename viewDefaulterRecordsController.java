package org.librarymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;

import static java.time.LocalDate.now;

public class viewDefaulterRecordsController implements Initializable {

    @FXML
    private TableView<Record> defaulterListTable;

    @FXML
    private TableColumn<Number, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> sNameColumn;
    @FXML
    private TableColumn<Book, String> bNameColumn;
    @FXML
    private TableColumn<Date, String> issueDateColumn;
    @FXML
    private TableColumn<Date, String> dueDateColumn;
    @FXML
    private TableColumn<String, String> statusColumn;


    @FXML
    private Label
            backLblDefaulterList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bNameColumn.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        sNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("issue_date"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("due_date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadIssueBooksTableData();
    }

    @FXML
    private void moveBack() throws IOException {
        UtilityMethods methods = new UtilityMethods();
        methods.moveBack(backLblDefaulterList);
    }

    @FXML
    private void close() {
        UtilityMethods.close(backLblDefaulterList);
    }

    private void loadIssueBooksTableData() {
        LocalDate date = now();

        String query = "SELECT * FROM issue_book_details where due_date < ? and status = ?";

        try
        {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setDate(1, java.sql.Date.valueOf(date));
            pst.setString(2,"pending");
            ResultSet rs = pst.executeQuery() ;

            Vector<Record> records = new Vector<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String bookName = rs.getString("book_name");
                String studentName = rs.getString("name");
                Date issueDate = rs.getDate("issue_date");
                Date dueDate = rs.getDate("due_date");
                String status = rs.getString("status");

                records.add(new Record(id,bookName, studentName, issueDate, dueDate,status));
            }
            ObservableList<Record> recordsList = FXCollections.observableArrayList(records);
            defaulterListTable.setItems(recordsList);
            defaulterListTable.setPlaceholder(new Label("No Record Found!"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
