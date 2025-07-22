
package DAO;

import dal.DBContext;
import model.Category;
import model.SubCategory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Categories table
 */
public class CategoryDAO {
    private final DBContext dbContext = new DBContext();

    /**
     * Retrieves all categories from the database
     * @return List of Category objects
     * @throws SQLException if a database error occurs
     */
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        
        try (
             PreparedStatement stmt = dbContext.getConnection().prepareStatement(
                     "SELECT CategoryId, CategoryName FROM Categories ORDER BY CategoryName");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("CategoryId"));
                category.setCategoryName(rs.getString("CategoryName"));
                categories.add(category);
            }
        }
        
        return categories;
    }
    
    /**
     * Retrieves categories with sorting and optional filtering
     * @param sortBy Column to sort by (default: CategoryId)
     * @param searchTerm Optional search term to filter categories
     * @return List of Category objects
     * @throws SQLException if a database error occurs
     */
    public List<Category> getCategories(String sortBy, String searchTerm) throws SQLException {
        List<Category> categories = new ArrayList<>();
        
        String validSortColumns = "CategoryId, CategoryName";
        if (sortBy == null || !validSortColumns.contains(sortBy)) {
            sortBy = "CategoryId";
        }
        
        StringBuilder sql = new StringBuilder(
            "SELECT CategoryId, CategoryName FROM Categories ");
        
        boolean hasSearchTerm = searchTerm != null && !searchTerm.trim().isEmpty();
        if (hasSearchTerm) {
            sql.append("WHERE CategoryName LIKE ? ");
        }
        
        sql.append("ORDER BY ").append(sortBy).append(" ASC");
        
        try (
             PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql.toString())) {
            
            if (hasSearchTerm) {
                stmt.setString(1, "%" + searchTerm.trim() + "%");
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category();
                    category.setCategoryId(rs.getInt("CategoryId"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    categories.add(category);
                }
            }
        }
        
        return categories;
    }
    
    /**
     * Adds a new category to the database
     * @param category The category to add
     * @return true if successful, false otherwise
     */
    public boolean addCategory(Category category) {
        if (category == null) {
            return false;
        }
        
        String sql = "INSERT INTO Categories (CategoryName) VALUES (?)";
        
        try (
             PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            
            stmt.setString(1, category.getCategoryName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates an existing category in the database
     * @param category The category to update
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        if (category == null) {
            return false;
        }
        
        String sql = "UPDATE Categories SET CategoryName = ? WHERE CategoryId = ?";
        
        try (
             PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            
            stmt.setString(1, category.getCategoryName());
            stmt.setInt(2, category.getCategoryId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes a category from the database
     * @param id The ID of the category to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCategory(int id) {
        String checkSql = "SELECT COUNT(*) FROM SubCategories WHERE CategoryId = ?";
        
        try (
             PreparedStatement checkStmt = dbContext.getConnection().prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
            
            String deleteSql = "DELETE FROM Categories WHERE CategoryId = ?";
            try (PreparedStatement deleteStmt = dbContext.getConnection().prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, id);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets a category by its ID
     * @param id The ID of the category to retrieve
     * @return The Category object, or null if not found
     */
    public Category getCategoryById(int id) {
        String sql = "SELECT CategoryId, CategoryName FROM Categories WHERE CategoryId = ?";
        
        try (
             PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setCategoryId(rs.getInt("CategoryId"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    return category;
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all subcategories with their category names
     * @return List of SubCategory objects with category names
     * @throws SQLException if a database error occurs
     */
    public List<SubCategory> getAllSubCategories() throws SQLException {
        List<SubCategory> subCategories = new ArrayList<>();
        String sql = """
            SELECT sc.*, c.CategoryName 
            FROM SubCategories sc
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            ORDER BY c.CategoryName, sc.SubCategoryName
        """;
        
        try (
             PreparedStatement stmt = dbContext.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setSubCategoryId(rs.getInt("SubCategoryId"));
                subCategory.setSubCategoryName(rs.getString("SubCategoryName"));
                subCategory.setCategoryId(rs.getInt("CategoryId"));
                subCategory.setCategoryName(rs.getString("CategoryName"));
                
                subCategories.add(subCategory);
            }
        }
        
        return subCategories;
    }
}
