/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author anhdu
 */
public class Notification {

    private int notificationId;
    private Integer userId; // null nếu toàn hệ thống
    private int typeId;
    private String typeName;
    private String title;
    private String message;
    private String url;
    private Integer relatedId; // ID của đối tượng liên quan
    private String relatedType; // Loại đối tượng liên quan
    private boolean isRead;
    private String priority;
    private Timestamp createdAt;

    public Notification() {
    }

    public Notification(int notificationId, Integer userId, int typeId, String title, String message, String url, 
                       Integer relatedId, String relatedType, boolean isRead, String priority, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.typeId = typeId;
        this.title = title;
        this.message = message;
        this.url = url;
        this.relatedId = relatedId;
        this.relatedType = relatedType;
        this.isRead = isRead;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
