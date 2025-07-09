package model;

public class RequestDetailItem {
    private int requestId;
    private int materialId;
    private String materialName;
    private String requestTypeName;
    private int quantity;
    private int actualQuantity;
    private String note;
    private int stockQuantity;
    private double price;
    private String status;

    // Default constructor
    public RequestDetailItem() {}

    // Full constructor
    public RequestDetailItem(int requestId, int materialId, String materialName,
                             String requestTypeName, int quantity, int actualQuantity,
                             String note, int stockQuantity, double price, String status) {
        this.requestId = requestId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.requestTypeName = requestTypeName;
        this.quantity = quantity;
        this.actualQuantity = actualQuantity;
        this.note = note;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RequestDetailItem{" +
                "requestId=" + requestId +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", requestTypeName='" + requestTypeName + '\'' +
                ", quantity=" + quantity +
                ", actualQuantity=" + actualQuantity +
                ", note='" + note + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
