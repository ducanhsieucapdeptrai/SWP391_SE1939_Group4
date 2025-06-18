package model;

public class MaterialStatus {
    private int statusId;
    private String statusName;
    private String description;

    public MaterialStatus() {
    }

    public MaterialStatus(int statusId, String statusName, String description) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.description = description;
    }

    // Getters and Setters
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MaterialStatus{" +
                "statusId=" + statusId +
                ", statusName='" + statusName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
