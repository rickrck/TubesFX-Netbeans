/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javafx.event.ActionEvent;

public abstract interface InterfaceCRUD {
    public abstract void addTask(ActionEvent event);
    public abstract void deleteTask(ActionEvent event);
    public abstract void updateTask(ActionEvent event);
}
