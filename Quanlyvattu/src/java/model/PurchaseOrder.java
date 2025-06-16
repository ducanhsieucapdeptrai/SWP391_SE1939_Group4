/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author thinh
 */
import java.util.Date;
import java.util.List;

public class PurchaseOrder {
    private int purchaseOrderId;
    private RequestList request;
    private Users createdBy;
    private String note;
    private String status;
    private Date createdAt;
    private List<PurchaseOrderDetail> details;

    public PurchaseOrder() {
    }

    public PurchaseOrder(int purchaseOrderId, RequestList request, Users createdBy, String note, String status, Date createdAt, List<PurchaseOrderDetail> details) {
        this.purchaseOrderId = purchaseOrderId;
        this.request = request;
        this.createdBy = createdBy;
        this.note = note;
        this.status = status;
        this.createdAt = createdAt;
        this.details = details;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public RequestList getRequest() {
        return request;
    }

    public void setRequest(RequestList request) {
        this.request = request;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<PurchaseOrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<PurchaseOrderDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" + "purchaseOrderId=" + purchaseOrderId + ", request=" + request + ", createdBy=" + createdBy + ", note=" + note + ", status=" + status + ", createdAt=" + createdAt + ", details=" + details + '}';
    }

    
}