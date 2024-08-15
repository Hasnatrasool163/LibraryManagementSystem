/**
 * @author MuhammadHasnatRasool
 * date: 9/8/2024
 * ReturnBooks controller for LMS
 */

// package declaration
package org.librarymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class ReturnBooksController {

    @FXML
    Label
            backLblIssueBook,
            bIdLbl,bNameLbl,issueIdLbl,
            sNameLbl,issueDateLbl,dueDateLbl,bErrorLbl;

    @FXML
    private TextField
            bIdField,sIdField;

    @FXML Button
            issueBtn,findBtn;


    public void close() {
        UtilityMethods.close(backLblIssueBook);
    }

    // method to move back to home-page

    public void moveBack() throws IOException {

        backLblIssueBook.getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
        Scene scene = new Scene(root);
        Image icon = new Image(String.valueOf(Objects.requireNonNull(getClass().getResource("home.png"))));
        stage.getIcons().add(icon);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("home.css")).toExternalForm());
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Home-Page-LMS");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    public boolean getIssueBookDetails(){

        boolean success = false;
        int bookId = Integer.parseInt(bIdField.getText());
        int studentId = Integer.parseInt(sIdField.getText());


        try{
            Connection con = DBConnection.getConnection();
            String sql ="select * from issue_book_details where book_id =? and student_id=? and status =?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1,bookId);
            pst.setInt(2,studentId);
            pst.setString(3,"pending");

            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                success=true;
                issueIdLbl.setText(rs.getString("id"));
                bNameLbl.setText(rs.getString("book_name"));
                sNameLbl.setText(rs.getString("name"));
                issueDateLbl.setText(String.valueOf(rs.getDate("issue_date")));
                dueDateLbl.setText(String.valueOf(rs.getDate("due_date")));
                bErrorLbl.setText("");
            }
            else{ // if record not present
                success=false;
                bErrorLbl.setText("No Record Found!");
                issueIdLbl.setText("");
                bNameLbl.setText("");
                sNameLbl.setText("");
                issueDateLbl.setText("");
                dueDateLbl.setText("");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    public void findButtonActionPerformed(){
        bErrorLbl.setText("");
        getIssueBookDetails();
    }

    public boolean returnBook(){
        boolean isReturned=false;

        int bookId = Integer.parseInt(bIdField.getText());
        int studentId = Integer.parseInt(sIdField.getText());


        try {
            Connection con = DBConnection.getConnection();
            String sql = "update issue_book_details set status=? where student_id =? and book_id =? and status=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "returned");
            pst.setInt(2,studentId);
            pst.setInt(3,bookId);
            pst.setString(4,"pending");

            int rowCount = pst.executeUpdate();
            isReturned=rowCount>0;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return isReturned;
    }

    public void returnButtonActionPerformed(){
        if(returnBook()){
            showAlert(Alert.AlertType.CONFIRMATION,"Book-Returned","Book Successfully Returned!","Book-Return");
            updateBookCount();
            clear();
        }
        else{
            showAlert(Alert.AlertType.ERROR,"Book-Returned","Book Return Failed!","Book-Return");

        }
    }
    public void updateBookCount(){

        int bookId = Integer.parseInt(bIdField.getText());
        try{
            Connection con = DBConnection.getConnection();
            String sql = "update book_details set quantity = quantity +1 where book_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1,bookId);

            int rowCount = pst.executeUpdate();
            if(rowCount>0){
                bErrorLbl.setText("Book Count Updated!");
//                showAlert(Alert.AlertType.INFORMATION,"Issue-Books","Book count updated!","Book");
            }
            else{
                showAlert(Alert.AlertType.ERROR,"Issue-Books","could not update Book count!","Book");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showAlert(Alert.AlertType type,String title, String content,String header) {
        Alert alert = new Alert(type);
        alert.setTitle("Manage-Books");
        alert.setContentText(content);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.initOwner(backLblIssueBook.getScene().getWindow());
        alert.show();
    }


    public void clear(){
        issueIdLbl.setText("");
        bNameLbl.setText("");
        sNameLbl.setText("");
        issueDateLbl.setText("");
        dueDateLbl.setText("");
        bIdField.setText("");
        sIdField.setText("");
//        bErrorLbl.setText("");
    }
}
