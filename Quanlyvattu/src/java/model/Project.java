package model;

import java.sql.Date;

public class Project {
    private int projectId;
    private String projectName;
    private String description;
    private Date startDate;
    private Date endDate;
    private int managerId;
    private String status;
    private String attachmentPath; // ✅ MỚI: đường dẫn file

    private String managerName; // dùng khi JOIN để hiển thị

    // Constructors
    public Project() {}

    public Project(int projectId, String projectName, String description,
                   Date startDate, Date endDate, int managerId, String status) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.managerId = managerId;
        this.status = status;
    }

    // Getters & Setters
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public String getAttachmentPath() { return attachmentPath; }
    public void setAttachmentPath(String attachmentPath) { this.attachmentPath = attachmentPath; }
}
