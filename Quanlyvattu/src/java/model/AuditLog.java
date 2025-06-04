package model;

import java.sql.Timestamp;

/**
 * Model class representing an audit log entry
 */
public class AuditLog {
    private int id;
    private String action;
    private int userId;
    private Timestamp timestamp;
    private String userName; // Optional: for display purposes

    public AuditLog() {
    }

    public AuditLog(int id, String action, int userId, Timestamp timestamp) {
        this.id = id;
        this.action = action;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
