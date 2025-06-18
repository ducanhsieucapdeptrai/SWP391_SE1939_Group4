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
public class ImportList {

    private int importId;
    private Date importDate;
    private int importedBy;
    private int importTypeId;
    private String note;

    private String importedByName;
    private String importTypeName;

    public ImportList() {
    }

    public ImportList(int importId, Date importDate, int importedBy, int importTypeId, String note, String importedByName, String importTypeName) {
        this.importId = importId;
        this.importDate = importDate;
        this.importedBy = importedBy;
        this.importTypeId = importTypeId;
        this.note = note;
        this.importedByName = importedByName;
        this.importTypeName = importTypeName;
    }

    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public int getImportedBy() {
        return importedBy;
    }

    public void setImportedBy(int importedBy) {
        this.importedBy = importedBy;
    }

    public int getImportTypeId() {
        return importTypeId;
    }

    public void setImportTypeId(int importTypeId) {
        this.importTypeId = importTypeId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImportedByName() {
        return importedByName;
    }

    public void setImportedByName(String importedByName) {
        this.importedByName = importedByName;
    }

    public String getImportTypeName() {
        return importTypeName;
    }

    public void setImportTypeName(String importTypeName) {
        this.importTypeName = importTypeName;
    }

}
