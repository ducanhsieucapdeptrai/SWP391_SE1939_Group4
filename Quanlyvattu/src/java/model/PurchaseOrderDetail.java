package model;

public class PurchaseOrderDetail {
    private int poId;          // <- NEW: ID của PurchaseOrder (khóa chính phụ)
    private int materialId;
    private int quantity;
    private double unitPrice;
    private Material material; // optional: thông tin material chi tiết

    public PurchaseOrderDetail() {}

    public PurchaseOrderDetail(int poId, int materialId, int quantity, double unitPrice, Material material) {
        this.poId = poId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.material = material;
    }

    public int getPoId() {
        return poId;
    }

    public void setPoId(int poId) {
        this.poId = poId;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public double getLineTotal() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return "PurchaseOrderDetail{" +
                "poId=" + poId +
                ", materialId=" + materialId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", material=" + material +
                '}';
    }
}
