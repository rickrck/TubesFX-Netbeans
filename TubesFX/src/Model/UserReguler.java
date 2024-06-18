/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

public class UserReguler extends User implements maxTask{
    
    public UserReguler(String nama, String username, String password, String status){
        super(nama, username, password, status);
    }
    
    public int getMaxTask(){
        return maxTask;
    }
}
