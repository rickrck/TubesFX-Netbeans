/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author ricky
 */
public class Task {
    
    private int ID;
    private String judul;
    private String desc;
    private Date startDate;
    private Date endDate;
    private String jenisTask;
    private String status;
    private String user;
    
    public Task(int ID, String judul, String desc, Date startDate, Date endDate, String jenisTask, String status, String user){
        this.ID = ID;
        this.judul = judul;
        this.desc = desc;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jenisTask = jenisTask;
        this.status = status;
        this.user = user;
    }
    
    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getJenisTask() {
        return jenisTask;
    }

    public void setJenisTask(String jenisTask) {
        this.jenisTask = jenisTask;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
