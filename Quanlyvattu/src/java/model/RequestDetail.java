package model;

/**
 * 
 * @author anhdu
 */
public class RequestDetail {

    private int requestId;
    private int materialId;
    private int quantity;
    
    
    private String materialName;
    private double price;
    private String image;
    private String description;
    private String subCategoryName;
    private String categoryName;

    public RequestDetail() {
    }

    public RequestDetail(int requestId, int materialId, int quantity) {
        this.requestId = requestId;
        this.materialId = materialId;
        this.quantity = quantity;
    }

    
    public RequestDetail(int requestId, int materialId, int quantity, String materialName, 
                        double price, String image, String description, String subCategoryName, 
                        String categoryName) {
        this.requestId = requestId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.materialName = materialName;
        this.price = price;
        this.image = image;
        this.description = description;
        this.subCategoryName = subCategoryName;
        this.categoryName = categoryName;
    }

   
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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

    
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    
    public double getTotalValue() {
        return quantity * price;
    }
}