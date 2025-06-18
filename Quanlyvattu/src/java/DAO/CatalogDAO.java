package DAO;

import dal.DBContext;
import model.Catalog;
import model.SubCategory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Categories table (used as catalog)
 */
public class CatalogDAO extends DBContext{
   


    /**
     * Retrieves all catalog items from the database
     * @return List of Catalog objects
     * @throws SQLException if a database error occurs
     */
    /**
     * Retrieves all catalog items from the database
     * @return List of Catalog objects
     * @throws SQLException if a database error occurs
     */
    public List<Catalog> getAllCatalogItems() throws SQLException {
        List<Catalog> categories = new ArrayList<>();
        
        try (
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT CategoryId, CategoryName FROM Categories ORDER BY CategoryName");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Catalog category = new Catalog();
                category.setId(rs.getInt("CategoryId"));
                category.setName(rs.getString("CategoryName"));
                category.setDescription(""); // Set empty description since the column doesn't exist
                categories.add(category);
            }
        }
        
        return categories;
    }
    
    /**
     * Retrieves catalog items with sorting and optional filtering
     * @param sortBy Column to sort by (default: CategoryId)
     * @param searchTerm Optional search term to filter categories
     * @return List of Catalog objects
     * @throws SQLException if a database error occurs
     */
    public List<Catalog> getCatalogItems(String sortBy, String searchTerm) throws SQLException {
        List<Catalog> catalogItems = new ArrayList<>();
        
        // Validate and sanitize sortBy parameter
        String validSortColumns = "CategoryId, CategoryName";
        if (sortBy == null || !validSortColumns.contains(sortBy)) {
            sortBy = "CategoryId";
        }
        
        // Build SQL query with optional search and sorting
        // Note: Using empty string for Description since the column doesn't exist in the database
        StringBuilder sql = new StringBuilder(
            "SELECT CategoryId, CategoryName FROM Categories ");
        
        // Add search condition if searchTerm is provided
        boolean hasSearchTerm = searchTerm != null && !searchTerm.trim().isEmpty();
        if (hasSearchTerm) {
            sql.append("WHERE CategoryName LIKE ? ");
        }
        
        // Add sorting
        sql.append("ORDER BY ").append(sortBy).append(" ASC");
        
        try (
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            
            // Set search parameter if provided
            if (hasSearchTerm) {
                stmt.setString(1, "%" + searchTerm.trim() + "%");
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Catalog item = new Catalog();
                    item.setId(rs.getInt("CategoryId"));
                    item.setName(rs.getString("CategoryName"));
                    item.setDescription(""); // Set empty description since the column doesn't exist
                    catalogItems.add(item);
                }
            }
        }
        
        return catalogItems;
    }
    
    /**
     * Adds a new catalog item to the database
     * @param item The catalog item to add
     * @return true if successful, false otherwise
     */
    public boolean addCatalogItem(Catalog item) {
        if (item == null) {
            return false;
        }
        
        String sql = "INSERT INTO Categories (CategoryName) VALUES (?)";
        
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, item.getName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates an existing catalog item in the database
     * @param item The catalog item to update
     * @return true if successful, false otherwise
     */
    public boolean updateCatalogItem(Catalog item) {
        if (item == null) {
            return false;
        }
        
        String sql = "UPDATE Categories SET CategoryName = ? WHERE CategoryId = ?";
        
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes a catalog item from the database
     * @param id The ID of the catalog item to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCatalogItem(int id) {
        // First check if there are any subcategories using this category
        String checkSql = "SELECT COUNT(*) FROM SubCategories WHERE CategoryId = ?";
        
        try (
             PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Cannot delete category with subcategories
                    return false;
                }
            }
            
            // If no subcategories, proceed with deletion
            String deleteSql = "DELETE FROM Categories WHERE CategoryId = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
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
     * Gets a catalog item by its ID
     * @param id The ID of the catalog item to retrieve
     * @return The Catalog object, or null if not found
     */
    public Catalog getCatalogItemById(int id) {
        String sql = "SELECT CategoryId, CategoryName FROM Categories WHERE CategoryId = ?";
        
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Catalog item = new Catalog();
                    item.setId(rs.getInt("CategoryId"));
                    item.setName(rs.getString("CategoryName"));
                    item.setDescription(""); // No description field in Categories table
                    return item;
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all categories
     * @return List of all categories
     * @throws SQLException if a database error occurs
     */
    public List<Catalog> getAllCategories() throws SQLException {
        return getAllCatalogItems();
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
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setSubCategoryId(rs.getInt("SubCategoryId"));
                subCategory.setSubCategoryName(rs.getString("SubCategoryName"));
                subCategory.setCategoryId(rs.getInt("CategoryId"));
                subCategory.setDescription(""); // SubCategories table does not have a Description column
                subCategory.setCategoryName(rs.getString("CategoryName"));
                
                subCategories.add(subCategory);
            }
        }
        
        return subCategories;
    }
}
