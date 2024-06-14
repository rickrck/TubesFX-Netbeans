/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Database.DatabaseConnect;
import Model.*;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ricky
 */
public class LoginController extends AppConstruct implements Initializable {

    @FXML
    private Button btnSignIn;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Hyperlink hl_register;
    
    DatabaseConnect database = new DatabaseConnect();
    private Connection conn;
    private PreparedStatement pr;
    private ResultSet rs;
    
    private Alert alert;

    /**
     * Initializes the controller class.
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void SignInClicked(ActionEvent event) throws SQLException {
        String selectData = "SELECT * FROM user WHERE username = '" 
                + tf_username.getText() + "'and password = '" + tf_password.getText() + "'";
        
        conn = database.getConnection();
        
        try{
            if (tf_username.getText().isEmpty() || tf_password.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR, "Username atau Password kosong");
                alert.setHeaderText(null);
                stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/Resource/icon.jpeg"));
                alert.showAndWait();
            } else {
                pr = conn.prepareStatement(selectData);
                rs = pr.executeQuery();
                
                if(rs.next()){
                    
                    User user = new User(rs.getString("nama"), rs.getString("username"), rs.getString("password"), rs.getString("status"));
                    if ("REGULER".equals(User.getStatus())){
                        user = new UserReguler(rs.getString("nama"), rs.getString("username"), rs.getString("password"), rs.getString("status"));
                        System.out.println("User Reguler");
                    } else if ("PREMIUM".equals(User.getStatus())){
                        user = new UserPremium(rs.getString("nama"), rs.getString("username"), rs.getString("password"), rs.getString("status"));
                        System.out.println("User Premium");
                    }
                    
                    alert = new Alert(AlertType.INFORMATION, "Login berhasil, mentang-mentang punya akun");
                    alert.setTitle("My Task Confirmation");
                    alert.setHeaderText(null);
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/Resource/icon.jpeg"));
                    alert.showAndWait();
                    
                    Parent root = FXMLLoader.load(getClass().getResource("/View/Dashboard.fxml"));
                    stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setTitle("MyTask");
                    stage.getIcons().add(new Image("Resource/icon.jpeg"));
                    stage.setResizable(false);
                    btnSignIn.getScene().getWindow().hide();
                    
                    
                } else {
                    alert = new Alert(AlertType.INFORMATION, "Username atau Password salah");
                    alert.setTitle("My Task Confirmation");
                    alert.setHeaderText(null);
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/Resource/icon.jpeg"));
                    stage.setResizable(false);
                    alert.showAndWait();
                }
            }
        } catch (SQLException e){
            e.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void switchRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Register.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setTitle("MyTask");
        stage.getIcons().add(new Image("Resource/icon.jpeg"));
        stage.setResizable(false);
        btnSignIn.getScene().getWindow().hide();
    }
}