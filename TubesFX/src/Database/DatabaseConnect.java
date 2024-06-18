package Database;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.*;

public class DatabaseConnect {
    public Connection conn;
    static final String DB_NAME = "mytask";
    static final String DB_USER = "root";
    static final String DB_PASS = "";
    
    static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
            e.printStackTrace();
            e.getCause();
        }
        return conn;
    }
}