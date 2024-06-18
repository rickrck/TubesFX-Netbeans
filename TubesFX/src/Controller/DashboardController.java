/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Database.DatabaseConnect;
import Model.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController extends AppConstruct implements Initializable, InterfaceCRUD {

    @FXML
    private Button btnHome;
    @FXML
    private Button btnTask;
    @FXML
    private AnchorPane Home_form;
    @FXML
    private AnchorPane Task_form;
    @FXML
    private TextArea Task_desc;
    @FXML
    private DatePicker Task_endDate;
    @FXML
    private TextField Task_judul;
    @FXML
    private DatePicker Task_startDate;
    @FXML
    private ComboBox<String> Task_jenisTask;
    @FXML
    private Button Task_add;
    @FXML
    private Button Task_update;
    @FXML
    private Button Task_delete;
    @FXML
    private TableView<Task> Task_tableView;
    @FXML
    private TableColumn<Task, String> Task_col_judul;
    @FXML
    private TableColumn<Task, String> Task_col_startDate;
    @FXML
    private TableColumn<Task, String> Task_col_endDate;
    @FXML
    private TableColumn<Task, String> Task_col_jenisTask;
    @FXML
    private TableColumn<Task, String> Task_col_status;
    @FXML
    private AnchorPane Overview_form;
    @FXML
    private Label labelWelcome;
    @FXML
    private Label labelStatus;
    DatabaseConnect database = new DatabaseConnect();
    private Connection conn;
    private PreparedStatement pr;
    private ResultSet rs;
    private Statement st;
    private ObservableList<Task> TaskLists;
    private int TaskID;
    private Alert alert;
    @FXML
    private Button btnLogout;
    @FXML
    private Label hasilUsername;
    @FXML
    private Label hasilNama;
    @FXML
    private Label hasilStatus;
    @FXML
    private Button switchToPremium;
    @FXML
    private Button Task_finish;
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        displayHeader();
        showTask();
        Task_jenisTask.setItems(FXCollections.observableArrayList("Belajar", "Tugas", "Ujian"));
    }
    
    public void displayHeader(){
        labelWelcome.setText(User.getNama());
        labelStatus.setText("User " + User.getStatus());
        hasilUsername.setText(User.getUsername());
        hasilNama.setText(User.getNama());
        hasilStatus.setText(User.getStatus());
    }
    
    public ObservableList<Task> TaskList() {
        ObservableList<Task> listTask = FXCollections.observableArrayList();

        String selectData = "SELECT * FROM task WHERE user = ? AND is_deleted = FALSE";

        conn = database.getConnection();

        try {
            pr = conn.prepareStatement(selectData);
            pr.setString(1, User.getUsername());
            rs = pr.executeQuery();

            Task task;
            while (rs.next()) {
                task = new Task(rs.getInt("ID"),
                                rs.getString("judul"),
                                rs.getString("deskripsi"),
                                rs.getDate("startDate"),
                                rs.getDate("endDate"),
                                rs.getString("jenisTask"),
                                rs.getString("status"),
                                rs.getString("user")
                );

                listTask.add(task);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listTask;
    }
    
    public void showTask(){
        
        TaskLists = TaskList();
        
        Task_col_judul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        Task_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        Task_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        Task_col_jenisTask.setCellValueFactory(new PropertyValueFactory<>("jenisTask"));
        Task_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        Task_tableView.setItems(TaskLists);
        
    }
    
    @FXML
    public void selectTask(){
        
        Task task = Task_tableView.getSelectionModel().getSelectedItem();
        int num = Task_tableView.getSelectionModel().getSelectedIndex();
        
        TaskID = task.getID();
        Task_judul.setText(task.getJudul());
        Task_desc.setText(task.getDesc());
        Task_startDate.setValue(LocalDate.parse(String.valueOf(task.getStartDate())));
        Task_endDate.setValue(LocalDate.parse(String.valueOf(task.getEndDate())));
        Task_jenisTask.setValue(String.valueOf(task.getJenisTask()));
        
    }
    
    @FXML
    private void switchForm(ActionEvent event) {
        if(event.getSource() == btnHome){
            Home_form.setVisible(true);
            Task_form.setVisible(false);
        } else if (event.getSource() == btnTask){
            Home_form.setVisible(false);
            Task_form.setVisible(true);
        }
    }

    @FXML
    @Override
    public void addTask(ActionEvent event) {
        conn = database.getConnection();
        
        try{
            if(Task_judul.getText().isEmpty() || Task_startDate.getValue() == null ||
                Task_endDate.getValue() == null || Task_jenisTask.getValue() == null){
                showAlert(Alert.AlertType.INFORMATION, "Tolong isi semua data yang dibutuhkan");
            } else {
                if(Task_endDate.getValue().isBefore(Task_startDate.getValue())){
                    showAlert(Alert.AlertType.ERROR, "Tanggal mulai melebihi tanggal selesai");
                } else {
                    String checkPlan = "SELECT  judul FROM task WHERE judul = '" +
                            Task_judul.getText() + "'";
                    
                    pr = conn.prepareStatement(checkPlan);
                    rs = pr.executeQuery();
                    
                    if("REGULER".equals(User.getStatus())){
                        if(TaskList().size() > UserReguler.maxTask){
                            showAlert(Alert.AlertType.ERROR, "User REGULER hanya bisa memiliki maksimal 10 Task");
                        } else {
                            if (rs.next()){
                                showAlert(Alert.AlertType.ERROR, "Judul task : " + Task_judul.getText() + " sudah ada");
                            } else {
                                String insertData = "INSERT INTO task (judul, deskripsi, startDate, endDate, jenisTask, status, user) VALUES (?,?,?,?,?,?,?)";
                    
                                pr = conn.prepareStatement(insertData);
                                pr.setString(1, Task_judul.getText());
                                pr.setString(2, Task_desc.getText());
                                pr.setString(3, String.valueOf(Task_startDate.getValue()));
                                pr.setString(4, String.valueOf(Task_endDate.getValue()));
                                pr.setString(5, String.valueOf(Task_jenisTask.getValue()));
                                pr.setString(6, "Not Finished");
                                pr.setString(7, User.getUsername());

                                pr.executeUpdate();

                                showAlert(Alert.AlertType.INFORMATION, "Task berhasil ditambahkan ke table MyTask");
                            }
                        }
                    } else {
                        if (rs.next()){
                            showAlert(Alert.AlertType.ERROR, "Judul task : " + Task_judul.getText() + " sudah ada");
                        } else {
                            String insertData = "INSERT INTO task (judul, deskripsi, startDate, endDate, jenisTask, status, user) VALUES (?,?,?,?,?,?,?)";

                            pr = conn.prepareStatement(insertData);
                            pr.setString(1, Task_judul.getText());
                            pr.setString(2, Task_desc.getText());
                            pr.setString(3, String.valueOf(Task_startDate.getValue()));
                            pr.setString(4, String.valueOf(Task_endDate.getValue()));
                            pr.setString(5, String.valueOf(Task_jenisTask.getValue()));
                            pr.setString(6, "Not Finished");
                            pr.setString(7, User.getUsername());

                            pr.executeUpdate();

                            showAlert(Alert.AlertType.INFORMATION, "Task berhasil ditambahkan ke table MyTask");
                        }
                    }
                    Task_judul.setText("");
                    Task_desc.setText("");
                    Task_startDate.setValue(null);
                    Task_endDate.setValue(null);
                    Task_jenisTask.setValue(null);
                    showTask();
                }
            }
        } catch (SQLException ex) { 
            if (ex instanceof SQLDataException) {
            alert = new Alert(Alert.AlertType.ERROR, "Data terlalu panjang atau tidak sesuai format yang diharapkan");
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Terjadi kesalahan saat mengakses database");
        }
        ex.printStackTrace();
        }
    }
    
    @Override
    @FXML
    public void deleteTask(ActionEvent event) {
        conn = database.getConnection();

        try {

            if (TaskID == 0) {
                showAlert(Alert.AlertType.ERROR, "Pilih tugas yang ingin dihapus terlebih dahulu");
            } else {
                if (showConfirmationAlert("Jadi hapus " + Task_judul.getText() + "?")) {
                    String checkData = "SELECT * FROM task WHERE id = " + TaskID;

                    st = conn.createStatement();
                    rs = st.executeQuery(checkData);
                    if (rs.next()) {

                        //String deleteData = "DELETE FROM task WHERE id = " + TaskID;

                        String softDeleteData = "UPDATE task SET is_deleted = TRUE WHERE id = " + TaskID;
                        
//                        pr = conn.prepareStatement(deleteData);
                        pr = conn.prepareStatement(softDeleteData);
                        pr.executeUpdate();

                        showAlert(Alert.AlertType.INFORMATION, "Task : " + Task_judul.getText() + " berhasil dihapus");

                        Task_judul.setText("");
                        Task_desc.setText("");
                        Task_startDate.setValue(null);
                        Task_endDate.setValue(null);
                        Task_jenisTask.setValue(null);
                        showTask();
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Task : " + Task_judul.getText() + " TIDAK jadi dihapus");
                }
            
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    @FXML
    public void updateTask(ActionEvent event) {
        conn = database.getConnection();

        try {

            if (TaskID == 0) {
                showAlert(Alert.AlertType.ERROR, "Pilih tugas yang ingin diupdate terlebih dahulu");
            } else {
                if (showConfirmationAlert("Jadi update " + Task_judul.getText() + "?")) {
                    String checkData = "SELECT * FROM task WHERE id = " + TaskID;

                    st = conn.createStatement();
                    rs = st.executeQuery(checkData);
                    if (rs.next()) {

                        String updateData = "UPDATE task SET judul = '"
                                + Task_judul.getText() + "', deskripsi = '" 
                                + Task_desc.getText() + "', startDate = '"
                                + Task_startDate.getValue() + "', endDate = '"
                                + Task_endDate.getValue() + "', jenisTask = '"
                                + Task_jenisTask.getValue() + "' WHERE id = " + TaskID;

                        pr = conn.prepareStatement(updateData);
                        pr.executeUpdate();

                        showAlert(Alert.AlertType.INFORMATION, "Task : " + Task_judul.getText() + " berhasil diupdate");

                        Task_judul.setText("");
                        Task_desc.setText("");
                        Task_startDate.setValue(null);
                        Task_endDate.setValue(null);
                        Task_jenisTask.setValue(null);
                        showTask();

                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Task : " + Task_judul.getText() + " tidak jadi diupdate");
                }
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            if (showConfirmationAlert("Anda ingin loogout dari akun?")) {
                btnLogout.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));

                Scene scene = new Scene(root);
                stage.setTitle("My Task : Login");
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void switchPremium(ActionEvent event) throws SQLException {
        conn = database.getConnection();

        try {

            if ("PREMIUM".equals(User.getStatus())){
                showAlert(Alert.AlertType.INFORMATION, "Akun anda sudah PREMIUM");
            } else {
                if (showConfirmationAlert("Akun anda ingin beralih menjadi user PREMIUM?")) {
                    String checkData = "SELECT * FROM user WHERE username = '" + User.getUsername() + "'";
                    st = conn.createStatement();
                    rs = st.executeQuery(checkData);
                    if (rs.next()) {
                        String updateData = "UPDATE user SET status = 'PREMIUM' WHERE username = '" + User.getUsername() + "'";
                        pr = conn.prepareStatement(updateData);
                        pr.executeUpdate();
                        
                        showAlert(Alert.AlertType.INFORMATION, "Akun anda berhasil diupdate, silahkan Login kembali");
                        
                        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                        stage.setTitle("MyTask");
                        stage.getIcons().add(new Image("Resource/icon.jpeg"));
                        stage.setResizable(false);
                        hasilNama.getScene().getWindow().hide();
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Akun anda tidak jadi beralih ke user PREMIUM");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void finishingTask(ActionEvent event) {
        
        conn = database.getConnection();

        try {

            if (TaskID == 0) {
                showAlert(Alert.AlertType.ERROR, "Pilih tugas yang ingin diselesaikan terlebih dahulu");
            } else {
                if (showConfirmationAlert("Kamu telah menyelesaikan " + Task_judul.getText() + "?")) {
                    String checkData = "SELECT * FROM task WHERE id = " + TaskID;

                    st = conn.createStatement();
                    rs = st.executeQuery(checkData);
                    if (rs.next()) {

                        String updateData = "UPDATE task SET status = 'FINISHED' WHERE id = '" + TaskID + "'";

                        pr = conn.prepareStatement(updateData);
                        pr.executeUpdate();

                        showAlert(Alert.AlertType.INFORMATION, Task_judul.getText() + " sudah FINISH");
                        
                        showTask();
                        
                        Task_judul.setText("");
                        Task_desc.setText("");
                        Task_startDate.setValue(null);
                        Task_endDate.setValue(null);
                        Task_jenisTask.setValue(null);

                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Status Task tidak berubah");
                }
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }
    
}
