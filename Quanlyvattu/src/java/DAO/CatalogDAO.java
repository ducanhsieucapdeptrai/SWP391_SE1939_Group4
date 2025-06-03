package DAO;

import dal.DBContext;
import model.Catalog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Categories table (used as catalog)
 */
public class CatalogDAO {
    private final DBContext dbContext;

    public CatalogDAO() {
        this.dbContext = new DBContext();
    }

    /**
     * Retrieves all catalog items from the database
     * @return List of Catalog objects
     * @throws SQLException if a database error occurs
     */
    public List<Catalog> getAllCatalogItems() throws SQLException {
        List<Catalog> catalogItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbContext.getConnection();
            
            // Using Categories table as catalog
            String sql = "SELECT CategoryId, CategoryName, '' as Description FROM Categories ORDER BY CategoryName";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Catalog item = new Catalog();
                item.setId(rs.getInt("CategoryId"));
                item.setName(rs.getString("CategoryName"));
                item.setDescription(rs.getString("Description"));
                
                catalogItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // Close resources in reverse order
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignored */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignored */ }
            }
            // Note: We don't close the connection as it's managed by DBContext
        }
        
        return catalogItems;
    }
    
    /**
     * Adds a new catalog item to the database
     * @param item The catalog item to add
     * @return true if successful, false otherwise
     */
    public boolean addCatalogItem(Catalog item) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbContext.getConnection();
            
            String sql = "INSERT INTO Categories (CategoryName) VALUES (?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, item.getName());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignored */ }
            }
            // Note: We don't close the connection as it's managed by DBContext
        }
    }
    
    /**
     * Updates an existing catalog item in the database
     * @param item The catalog item to update
     * @return true if successful, false otherwise
     */
    public boolean updateCatalogItem(Catalog item) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbContext.getConnection();
            
            String sql = "UPDATE Categories SET CategoryName = ? WHERE CategoryId = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignored */ }
            }
            // Note: We don't close the connection as it's managed by DBContext
        }
    }
    
    /**
     * Deletes a catalog item from the database
     * @param id The ID of the catalog item to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCatalogItem(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbContext.getConnection();
            
            // First check if there are any subcategories using this category
            String checkSql = "SELECT COUNT(*) FROM SubCategories WHERE CategoryId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            checkStmt.close();
            
            if (count > 0) {
                // Cannot delete category with subcategories
                return false;
            }
            
            String sql = "DELETE FROM Categories WHERE CategoryId = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignored */ }
            }
            // Note: We don't close the connection as it's managed by DBContext
        }
    }
    
    /**
     * Gets a catalog item by its ID
     * @param id The ID of the catalog item to retrieve
     * @return The Catalog object, or null if not found
     */
    public Catalog getCatalogItemById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbContext.getConnection();
            
            String sql = "SELECT CategoryId, CategoryName FROM Categories WHERE CategoryId = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Catalog item = new Catalog();
                item.setId(rs.getInt("CategoryId"));
                item.setName(rs.getString("CategoryName"));
                item.setDescription(""); // No description field in Categories table
                return item;
            }
            
            return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* ignored */ }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignored */ }
            }
            // Note: We don't close the connection as it's managed by DBContext
        }
    }
}
