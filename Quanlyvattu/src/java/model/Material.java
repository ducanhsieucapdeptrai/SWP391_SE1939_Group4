package model;

import java.sql.Timestamp;

public class Material {

    private int materialId;
    private String materialName;
    private int subCategoryId;
    private int statusId;
    private String image;
    private String description;
    private int quantity;
    private int minQuantity;
    private double price;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String categoryName;
    private String subCategoryName;
    private String statusName;

    public Material(int materialId, String materialName, int subCategoryId, int statusId, String image, String description, int quantity, int minQuantity, double price, Timestamp createdAt, Timestamp updatedAt, String categoryName, String subCategoryName, String statusName) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.subCategoryId = subCategoryId;
        this.statusId = statusId;
        this.image = image;
        this.description = description;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.statusName = statusName;
    }

    public Material() {
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    // Alias methods for JSP compatibility
    public Timestamp getCreatedDate() {
        return getCreatedAt();
}

    public Timestamp getLastUpdatedDate() {
        return getUpdatedAt();
}


   
}


