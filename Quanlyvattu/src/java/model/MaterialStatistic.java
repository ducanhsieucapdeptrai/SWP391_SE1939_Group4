package model;

import java.util.Date;

public class MaterialStatistic {
    private int materialId;
    private String materialName;
    private String categoryName;
    private String subCategoryName;
    private int initialStock;
    private int totalImported;
    private int totalExported;
    private int finalStock;
    private Date transactionDate;
    private int quantity;
    private String performedBy;
    private String note;
    private String transactionType;
    
    // Constructors
    public MaterialStatistic() {}
    
    // Getters and Setters
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getSubCategoryName() { return subCategoryName; }
    public void setSubCategoryName(String subCategoryName) { this.subCategoryName = subCategoryName; }
    
    public int getInitialStock() { return initialStock; }
    public void setInitialStock(int initialStock) { this.initialStock = initialStock; }
    
    public int getTotalImported() { return totalImported; }
    public void setTotalImported(int totalImported) { this.totalImported = totalImported; }
    
    public int getTotalExported() { return totalExported; }
    public void setTotalExported(int totalExported) { this.totalExported = totalExported; }
    
    public int getFinalStock() { return finalStock; }
    public void setFinalStock(int finalStock) { this.finalStock = finalStock; }
    
    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}