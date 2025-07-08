package model;

import java.sql.Timestamp;
import java.util.List;

public class TaskLog {
    private int taskId;
    private int requestId;
    private int requestTypeId;
    private int staffId;
    private Timestamp createdAt;

    private String staffName;
    private String requestTypeName;

    private List<TaskSlipDetail> slipDetails;

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getRequestTypeId() { return requestTypeId; }
    public void setRequestTypeId(int requestTypeId) { this.requestTypeId = requestTypeId; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public String getRequestTypeName() { return requestTypeName; }
    public void setRequestTypeName(String requestTypeName) { this.requestTypeName = requestTypeName; }

    public List<TaskSlipDetail> getSlipDetails() { return slipDetails; }
    public void setSlipDetails(List<TaskSlipDetail> slipDetails) { this.slipDetails = slipDetails; }
}
