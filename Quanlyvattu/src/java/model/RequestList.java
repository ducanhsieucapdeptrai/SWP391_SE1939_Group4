package model;

import java.util.Date;
import java.util.List;

public class RequestList {

    private int requestId;
    private int requestedBy;
    private Date requestDate;
    private int requestTypeId;
    private String note;
    private String status;
    private Integer approvedBy; // Có thể null nếu chưa duyệt
    private Date approvedDate;
    private String approvalNote;

    // Quan hệ bổ sung (không bắt buộc nhưng tiện)
    private Users requester;       // người yêu cầu
    private Users approver;        // người phê duyệt
    private RequestType requestType; // loại yêu cầu
    private List<RequestDetail> requestDetails; // chi tiết vật tư yêu cầu

    // Getters and Setters
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

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
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

    // Optional: Liên kết đến các đối tượng liên quan
    public Users getRequester() {
        return requester;
    }

    public void setRequester(Users requester) {
        this.requester = requester;
    }

    public Users getApprover() {
        return approver;
    }

    public void setApprover(Users approver) {
        this.approver = approver;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public List<RequestDetail> getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(List<RequestDetail> requestDetails) {
        this.requestDetails = requestDetails;
    }
}
