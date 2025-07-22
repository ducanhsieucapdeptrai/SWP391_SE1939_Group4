package model;

public class RepairOrderDetail {

    private int roId;
    private int materialId;
    private String materialName; // để hiển thị
    private int quantity;
    private double unitPrice; // estimated price người dùng nhập
    private String mnote;

    public int getRoId() {
        return roId;
    }

    public void setRoId(int roId) {
        this.roId = roId;
    }

    public String getMnote() {
        return mnote;
    }

    public void setMnote(String mnote) {
        this.mnote = mnote;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
