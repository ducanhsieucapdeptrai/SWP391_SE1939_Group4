package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for retrieving dashboard statistics
 */
public class DashboardDAO {
    
    /**
     * Get total number of materials in the system
     * @return total number of materials
     */
    public int getTotalMaterialCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Materials";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting total material count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * Get total number of imports
     * @return total number of imports
     */
    public int getTotalImportCount() {
        int count = 0;
        // Adjust the query based on your actual table name
        String sql = "SELECT COUNT(*) FROM ImportList";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting total import count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * Get total number of exports
     * @return total number of exports
     */
    public int getTotalExportCount() {
        int count = 0;
        // Adjust the query based on your actual table name
        String sql = "SELECT COUNT(*) FROM ExportList";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting total export count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * Get total number of pending requests
     * @return total number of pending requests
     */
    public int getPendingRequestCount() {
        int count = 0;
        // Adjust the query based on your actual table name and status field
        String sql = "SELECT COUNT(*) FROM RequestList WHERE Status = 'Pending'";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting pending request count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * Get today's sales count
     * @return today's sales count
     */
    public int getTodaySalesCount() {
        int count = 0;
        // Adjust the query based on your actual table name and date field
        String sql = "SELECT COUNT(*) FROM ExportList WHERE DATE(ExportDate) = CURDATE()";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting today's sales count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * Get total sales count
     * @return total sales count
     */
    public int getTotalSalesCount() {
        int count = 0;
        // Adjust the query based on your actual table name
        String sql = "SELECT COUNT(*) FROM ExportList";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting total sales count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return count;
    }
    
    /**
     * Get today's revenue from exports
     * @return today's revenue
     */
    public double getTodayRevenue() {
        double revenue = 0;
        // Adjust the query based on your actual table structure
        String sql = "SELECT SUM(e.Quantity * m.Price) AS TodayRevenue " +
                    "FROM ExportDetail e " +
                    "JOIN Materials m ON e.MaterialId = m.MaterialId " +
                    "JOIN ExportList el ON e.ExportId = el.ExportId " +
                    "WHERE DATE(el.ExportDate) = CURDATE()";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                revenue = rs.getDouble("TodayRevenue");
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting today's revenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        return revenue;
    }
    
    /**
     * Get total revenue from exports
     * @return total revenue
     */
    public double getTotalRevenue() {
        double revenue = 0;
        // Adjust the query based on your actual table structure
        String sql = "SELECT SUM(e.Quantity * m.Price) AS TotalRevenue " +
                    "FROM ExportDetail e " +
                    "JOIN Materials m ON e.MaterialId = m.MaterialId";
        
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                revenue = rs.getDouble("TotalRevenue");
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting total revenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        return revenue;
    }
}
