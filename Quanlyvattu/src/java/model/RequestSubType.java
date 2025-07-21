/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class RequestSubType {
    private int subTypeId;
    private int requestTypeId;
    private String subTypeName;
    private String description;

    // Default constructor
    public RequestSubType() {
    }

    // Full constructor
    public RequestSubType(int subTypeId, int requestTypeId, String subTypeName, String description) {
        this.subTypeId = subTypeId;
        this.requestTypeId = requestTypeId;
        this.subTypeName = subTypeName;
        this.description = description;
    }

    public RequestSubType(int subTypeId, int requestTypeId, String subTypeName) {
        this.subTypeId = subTypeId;
        this.requestTypeId = requestTypeId;
        this.subTypeName = subTypeName;
    }

    // Constructor without id for insertion
    public RequestSubType(int requestTypeId, String subTypeName, String description) {
        this.requestTypeId = requestTypeId;
        this.subTypeName = subTypeName;
        this.description = description;
    }

    // Getters and Setters
    public int getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(int subTypeId) {
        this.subTypeId = subTypeId;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RequestSubType{" +
                "subTypeId=" + subTypeId +
                ", requestTypeId=" + requestTypeId +
                ", subTypeName='" + subTypeName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
