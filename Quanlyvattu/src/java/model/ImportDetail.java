/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author anhdu
 */
public class ImportDetail {

    private int importDetailId;
    private int importId;
    private int materialId;
    private int quantity;
    private double price;

    private String materialName;

    public ImportDetail() {
    }

    public ImportDetail(int importDetailId, int importId, int materialId, int quantity, double price, String materialName) {
        this.importDetailId = importDetailId;
        this.importId = importId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.price = price;
        this.materialName = materialName;
    }

    public int getImportDetailId() {
        return importDetailId;
    }

    public void setImportDetailId(int importDetailId) {
        this.importDetailId = importDetailId;
    }

    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    
    
}
