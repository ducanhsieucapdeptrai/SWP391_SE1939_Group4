/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhdu
 */
public class ImportType {

    private int importTypeId;
    private String importTypeName;

    public ImportType() {
    }

    public ImportType(int importTypeId, String importTypeName) {
        this.importTypeId = importTypeId;
        this.importTypeName = importTypeName;
    }

    public int getImportTypeId() {
        return importTypeId;
    }

    public void setImportTypeId(int importTypeId) {
        this.importTypeId = importTypeId;
    }

    public String getImportTypeName() {
        return importTypeName;
    }

    public void setImportTypeName(String importTypeName) {
        this.importTypeName = importTypeName;
    }

}
