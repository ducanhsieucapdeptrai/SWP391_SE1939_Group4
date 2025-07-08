package model;

public class TaskSlipDetail {
    private int materialId;
    private String materialName;
    private int quantity;

    public TaskSlipDetail() {}

    public TaskSlipDetail(int materialId, String materialName, int quantity) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.quantity = quantity;
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
}
