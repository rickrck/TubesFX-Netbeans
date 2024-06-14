/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author ricky
 */
public class User {
    
    private static String nama;
    private static String username;
    private String password;
    private static String status;

    public User(String nama, String username, String password, String status){
        User.nama = nama;
        User.username = username;
        this.password = password;
        User.status = status;
    }
    
    public static String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        User.nama = nama;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        User.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        User.status = status;
    }
    
}
