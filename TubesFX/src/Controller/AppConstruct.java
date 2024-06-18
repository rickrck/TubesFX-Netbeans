/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppConstruct {
    
    public Stage stage;

    public AppConstruct(){
        stage = new Stage();
    }
    
    public void showAlert(Alert.AlertType alertType, String message){
        Alert alert = new Alert(alertType, message);
        String info = null;
       
        alert.setHeaderText(null);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/Resource/icon.jpeg"));
        
        if(null != alertType)switch (alertType) {
            case ERROR:
                info = "ERROR";
                break;
            case INFORMATION:
                info = "INFORMATION";
                break;
            default:
                break;
        }
        
        stage.setTitle("My Task : " + info);
        alert.showAndWait();
    }
    
    public boolean showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.setTitle("My Task : CONFIRMATION");
        alert.setHeaderText(null);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/Resource/icon.jpeg"));

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
