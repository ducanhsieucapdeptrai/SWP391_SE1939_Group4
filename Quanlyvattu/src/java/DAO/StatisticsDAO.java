package DAO;

import dal.DBContext;
import model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for statistics-related queries
 */
public class StatisticsDAO {
    private final DBContext dbContext;

    public StatisticsDAO() {
        this.dbContext = new DBContext();
    }

    /**
     * Retrieves all materials from the database
     * @return List of Material objects
     * @throws SQLException if a database error occurs
     */
    public List<Material> getAllMaterials() throws SQLException {
        List<Material> materials = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbContext.getConnection();
            
            String sql = "SELECT m.*, ms.StatusName FROM Materials m " +
                         "LEFT JOIN MaterialStatus ms ON m.StatusId = ms.StatusId " +
                         "ORDER BY m.MaterialName";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Material material = new Material();
                material.setMaterialId(rs.getInt("MaterialId"));
                material.setMaterialName(rs.getString("MaterialName"));
                material.setStatusId(rs.getInt("StatusId"));
                material.setStatusName(rs.getString("StatusName"));
                material.setQuantity(rs.getInt("Quantity"));
                material.setMinQuantity(rs.getInt("MinQuantity"));
                material.setPrice(rs.getDouble("Price"));
                material.setDescription(rs.getString("Description"));
                material.setImage(rs.getString("Image"));
                
                materials.add(material);
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
        
        return materials;
    }
    
    /**
     * Counts materials grouped by status
     * @return Map of status to count
     * @throws SQLException if a database error occurs
     */
    public Map<String, Integer> getMaterialCountByStatus() throws SQLException {
        Map<String, Integer> countByStatus = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbContext.getConnection();
            
            String sql = "SELECT ms.StatusName, COUNT(*) as count FROM Materials m " +
                         "JOIN MaterialStatus ms ON m.StatusId = ms.StatusId " +
                         "GROUP BY ms.StatusName";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String status = rs.getString("StatusName");
                int count = rs.getInt("count");
                countByStatus.put(status, count);
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
        
        return countByStatus;
    }
    
    /**
     * Counts materials grouped by category
     * @return Map of category to count
     * @throws SQLException if a database error occurs
     */
    public Map<String, Integer> getMaterialCountByType() throws SQLException {
        Map<String, Integer> countByType = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbContext.getConnection();
            
            String sql = "SELECT c.CategoryName, COUNT(*) as count FROM Materials m " +
                         "JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId " +
                         "JOIN Categories c ON sc.CategoryId = c.CategoryId " +
                         "GROUP BY c.CategoryName";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String category = rs.getString("CategoryName");
                int count = rs.getInt("count");
                countByType.put(category, count);
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
        
        return countByType;
    }
}
