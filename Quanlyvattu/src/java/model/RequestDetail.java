package model;

public class RequestDetail {

    private int requestId;
    private int materialId;
    private int quantity;
    private String note;

    private String materialName;
    private String image;
    private String description;
    private String subCategoryName;
    private String categoryName;
    private String unitName;

    private Material material;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public RequestDetail() {
    }

    public RequestDetail(int requestId, int materialId, int quantity) {
        this.requestId = requestId;
        this.materialId = materialId;
        this.quantity = quantity;
    }

    public RequestDetail(int requestId, int materialId, int quantity, String note) {
        this.requestId = requestId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.note = note;
    }

    public RequestDetail(int requestId, int materialId, int quantity, String materialName,
            String image, String description, String subCategoryName,
            String categoryName, String note) {
        this.requestId = requestId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.materialName = materialName;
        this.image = image;
        this.description = description;
        this.subCategoryName = subCategoryName;
        this.categoryName = categoryName;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
