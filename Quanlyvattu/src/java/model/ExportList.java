/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author anhdu
 */
public class ExportList {

    private int exportId;
    private Date exportDate;
    private int exportedBy;
    private int exportTypeId;
    private String note;

    private String exportedByName;
    private String exportTypeName;

    public ExportList() {
    }

    public ExportList(int exportId, Date exportDate, int exportedBy, int exportTypeId, String note, String exportedByName, String exportTypeName) {
        this.exportId = exportId;
        this.exportDate = exportDate;
        this.exportedBy = exportedBy;
        this.exportTypeId = exportTypeId;
        this.note = note;
        this.exportedByName = exportedByName;
        this.exportTypeName = exportTypeName;
    }

    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public int getExportedBy() {
        return exportedBy;
    }

    public void setExportedBy(int exportedBy) {
        this.exportedBy = exportedBy;
    }

    public int getExportTypeId() {
        return exportTypeId;
    }

    public void setExportTypeId(int exportTypeId) {
        this.exportTypeId = exportTypeId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExportedByName() {
        return exportedByName;
    }

    public void setExportedByName(String exportedByName) {
        this.exportedByName = exportedByName;
    }

    public String getExportTypeName() {
        return exportTypeName;
    }

    public void setExportTypeName(String exportTypeName) {
        this.exportTypeName = exportTypeName;
    }
    
    
}
