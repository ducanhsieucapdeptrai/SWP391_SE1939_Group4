/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author anhdu
 */
public class RepairOrder {
    private int roId;
    private int requestId;
    private String createdByName;
    private Date createdDate;
    private double totalPrice;
    private String status;
    private String note;

    public RepairOrder() {
    }

    public RepairOrder(int roId, int requestId, String createdByName, Date createdDate, double totalPrice, String status, String note) {
        this.roId = roId;
        this.requestId = requestId;
        this.createdByName = createdByName;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.note = note;
    }

    public int getRoId() {
        return roId;
    }

    public void setRoId(int roId) {
        this.roId = roId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
