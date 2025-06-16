/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhdu
 */
public class ExportType {

    private int exportTypeId;
    private String exportTypeName;
    private String description;

    public ExportType() {
    }

    public ExportType(int exportTypeId, String exportTypeName, String description) {
        this.exportTypeId = exportTypeId;
        this.exportTypeName = exportTypeName;
        this.description = description;
    }

    public int getExportTypeId() {
        return exportTypeId;
    }

    public void setExportTypeId(int exportTypeId) {
        this.exportTypeId = exportTypeId;
    }

    public String getExportTypeName() {
        return exportTypeName;
    }

    public void setExportTypeName(String exportTypeName) {
        this.exportTypeName = exportTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
