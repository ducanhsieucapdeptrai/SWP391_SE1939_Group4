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
    private int approvedBy;
    private Date approvedDate;
    private String approvalNote;

    private int assignedStaffId;
    private String assignedStaffName;

    private String requestedByName;
    private String approvedByName;
    private String requestTypeName;
    private String statusDescription;

    private RequestType requestType;
    private Date finishedDate;
    private String requestedByName;     // Tuỳ chọn: dùng để hiển thị tên người gửi
    private String approvedByName;      // Tuỳ chọn: dùng để hiển thị tên người duyệt
    private String requestTypeName;     // Tuỳ chọn: để hiển thị tên loại yêu cầu
    private String statusDescription;   // Tuỳ chọn: mô tả trạng thái từ RequestStatus
    private int projectId;
    private List<RequestDetail> requestDetails; // Tuỳ chọn: dùng nếu cần load chi tiết đi kèm
    private String subTypeName;
    private Date arrivalDate;
    private boolean isUpdated;
    private boolean isCompleted;
    private boolean isTransferredToday;

    // ✅ Purchase Order-related fields
    private boolean hasPO;
    private int poCount;
    private String poStatus;

    // 🔧 Repair Order-related fields
    private boolean hasRO;
    private int roCount;
    private String roStatus;

    public RequestList() {
    }

    public RequestList(int requestId, int requestedBy, Date requestDate, int requestTypeId, String note, String status, int approvedBy, Date approvedDate, String approvalNote, int assignedStaffId, String assignedStaffName, String requestedByName, String approvedByName, String requestTypeName, String statusDescription, RequestType requestType, List<RequestDetail> requestDetails, String subTypeName, Date arrivalDate, boolean isUpdated, boolean isCompleted, boolean isTransferredToday, boolean hasPO, int poCount, String poStatus, boolean hasRO, int roCount, String roStatus) {
        this.requestId = requestId;
        this.requestedBy = requestedBy;
        this.requestDate = requestDate;
        this.requestTypeId = requestTypeId;
        this.note = note;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approvedDate = approvedDate;
        this.approvalNote = approvalNote;
        this.assignedStaffId = assignedStaffId;
        this.assignedStaffName = assignedStaffName;
        this.requestedByName = requestedByName;
        this.approvedByName = approvedByName;
        this.requestTypeName = requestTypeName;
        this.statusDescription = statusDescription;
        this.requestType = requestType;
        this.requestDetails = requestDetails;
        this.subTypeName = subTypeName;
        this.arrivalDate = arrivalDate;
        this.isUpdated = isUpdated;
        this.isCompleted = isCompleted;
        this.isTransferredToday = isTransferredToday;
        this.hasPO = hasPO;
        this.poCount = poCount;
        this.poStatus = poStatus;
        this.hasRO = hasRO;
        this.roCount = roCount;
        this.roStatus = roStatus;
    }

    public boolean isHasRO() {
        return hasRO;
    }

    public void setHasRO(boolean hasRO) {
        this.hasRO = hasRO;
    }

    public int getRoCount() {
        return roCount;
    }

    public void setRoCount(int roCount) {
        this.roCount = roCount;
    }

    public String getRoStatus() {
        return roStatus;
    }

    public void setRoStatus(String roStatus) {
        this.roStatus = roStatus;
    }

   

    // ==== GETTERS & SETTERS ====
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

    public int getAssignedStaffId() {
        return assignedStaffId;
    }

    public void setAssignedStaffId(int assignedStaffId) {
        this.assignedStaffId = assignedStaffId;
    }

    public String getAssignedStaffName() {
        return assignedStaffName;
    }

    public void setAssignedStaffName(String assignedStaffName) {
        this.assignedStaffName = assignedStaffName;
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

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public boolean isIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isIsTransferredToday() {
        return isTransferredToday;
    }

    public void setIsTransferredToday(boolean isTransferredToday) {
        this.isTransferredToday = isTransferredToday;
    }
    public Date getFinishedDate() {
        return finishedDate;
    }

    public boolean isHasPO() {
        return hasPO;
    }

    public void setHasPO(boolean hasPO) {
        this.hasPO = hasPO;
    }

    public int getPoCount() {
        return poCount;
    }

    public void setPoCount(int poCount) {
        this.poCount = poCount;
    }

    public String getPoStatus() {
        return poStatus;
    }

    public void setPoStatus(String poStatus) {
        this.poStatus = poStatus;
    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }
    public int getProjectId() {
    return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
