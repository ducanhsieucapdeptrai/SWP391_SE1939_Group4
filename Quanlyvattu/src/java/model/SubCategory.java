package model;

/**
 * Model class representing a SubCategory in the system
 */
public class SubCategory {
    private int subCategoryId;
    private String subCategoryName;
    private int categoryId;
    private String description;
    private String categoryName; // For display purposes

    // Default constructor
    public SubCategory() {
    }

    // Parameterized constructor
    public SubCategory(int subCategoryId, String subCategoryName, int categoryId, String description) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.categoryId = categoryId;
        this.description = description;
    }
    //for create request
    public SubCategory(int subCategoryId, String subCategoryName, int categoryId) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.categoryId = categoryId;
    }
    

    // Getters and Setters
    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public String toString() {
        return "SubCategory{" +
                "subCategoryId=" + subCategoryId +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", categoryId=" + categoryId +
                ", description='" + description + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
