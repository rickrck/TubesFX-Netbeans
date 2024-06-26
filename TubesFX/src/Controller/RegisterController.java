/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Database.DatabaseConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class RegisterController extends AppConstruct implements Initializable {

    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Button btnSignUp;
    @FXML
    private Hyperlink hl_login;
    @FXML
    private TextField tf_name;
    
    private Alert alert;
    
    DatabaseConnect database = new DatabaseConnect();
    private Connection conn;
    private PreparedStatement pr;
    private ResultSet rs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public enum Status{
        REGULER, PREMIUM
    }

    @FXML
    private void SignUpClicked(ActionEvent event) throws SQLException, IOException {
        
        String insertData = "INSERT INTO user (nama, username, password, status) VALUES(?,?,?,?)";
        conn = database.getConnection();
        
        try {
            if(tf_name.getText().isEmpty() || tf_username.getText().isEmpty() || tf_password.getText().isEmpty()){
                showAlert(AlertType.INFORMATION, "Isi data yang kosong");
            } else {
                String checkUsername = "SELECT username FROM user WHERE username = '"
                        + tf_username.getText() + "'";
                pr = conn.prepareStatement(checkUsername);
                rs = pr.executeQuery();
                
                if(rs.next()) {
                    showAlert(AlertType.INFORMATION, "Akun dengan username " + tf_username.getText() + " sudah dipakai");
                } else {
                    if (tf_password.getText().length() < 8) {
                        showAlert(AlertType.ERROR, "Password minimal 8 karakter");
                    } else {
                        pr = conn.prepareStatement(insertData);
                        pr.setString(1, tf_name.getText());
                        pr.setString(2, tf_username.getText());
                        pr.setString(3, tf_password.getText());
                        pr.setString(4, Status.REGULER.toString());
                        pr.executeUpdate();     
                        
                        showAlert(AlertType.INFORMATION, "Akun berhasil dibuat");
                        
                        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                        stage.setTitle("MyTask");
                        stage.getIcons().add(new Image("Resource/icon.jpeg"));
                        stage.setResizable(false);
                        btnSignUp.getScene().getWindow().hide();
                   }
                }
            }
        } catch (SQLException e){
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setTitle("My Task : Login");
        stage.getIcons().add(new Image("Resource/icon.jpeg"));
        stage.setResizable(false);
        btnSignUp.getScene().getWindow().hide();
    }
    
}
