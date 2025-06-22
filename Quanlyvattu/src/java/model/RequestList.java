/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author anhdu
 */
public class RequestList {

    private int requestId;
    private int requestedBy;
    private Date requestDate;
    private int requestTypeId;
    private String note;
    private String status;
    private int approvedBy;
    private Date approvedDate;
    private String approvalNote;

    private String requestedByName;     // Tuỳ chọn: dùng để hiển thị tên người gửi
    private String approvedByName;      // Tuỳ chọn: dùng để hiển thị tên người duyệt
    private String requestTypeName;     // Tuỳ chọn: để hiển thị tên loại yêu cầu
    private String statusDescription;   // Tuỳ chọn: mô tả trạng thái từ RequestStatus

    private List<RequestDetail> requestDetails; // Tuỳ chọn: dùng nếu cần load chi tiết đi kèm
    private String importTypeName;
    private String exportTypeName;

// getters & setters
    public RequestList() {
    }

    public RequestList(int requestId, int requestedBy, Date requestDate, int requestTypeId, String note, String status, int approvedBy, Date approvedDate, String approvalNote, String requestedByName, String approvedByName, String requestTypeName, String statusDescription, List<RequestDetail> requestDetails, String importTypeName, String exportTypeName) {
        this.requestId = requestId;
        this.requestedBy = requestedBy;
        this.requestDate = requestDate;
        this.requestTypeId = requestTypeId;
        this.note = note;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approvedDate = approvedDate;
        this.approvalNote = approvalNote;
        this.requestedByName = requestedByName;
        this.approvedByName = approvedByName;
        this.requestTypeName = requestTypeName;
        this.statusDescription = statusDescription;
        this.requestDetails = requestDetails;
        this.importTypeName = importTypeName;
        this.exportTypeName = exportTypeName;
    }

    public String getImportTypeName() {
        return importTypeName;
    }

    public void setImportTypeName(String importTypeName) {
        this.importTypeName = importTypeName;
    }

    public String getExportTypeName() {
        return exportTypeName;
    }

    public void setExportTypeName(String exportTypeName) {
        this.exportTypeName = exportTypeName;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(int requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
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

    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApprovalNote() {
        return approvalNote;
    }

    public void setApprovalNote(String approvalNote) {
        this.approvalNote = approvalNote;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public void setRequestedByName(String requestedByName) {
        this.requestedByName = requestedByName;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public List<RequestDetail> getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(List<RequestDetail> requestDetails) {
        this.requestDetails = requestDetails;
    }

    private boolean hasPO;

    public boolean isHasPO() {
        return hasPO;
    }

    public void setHasPO(boolean hasPO) {
        this.hasPO = hasPO;
    }

    private int poCount;

    public int getPoCount() {
        return poCount;
    }

    public void setPoCount(int poCount) {
        this.poCount = poCount;
    }

    private String poStatus;

    public String getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(String poStatus) {
        this.poStatus = poStatus;
    }

}
