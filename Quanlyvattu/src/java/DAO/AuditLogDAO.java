package DAO;

import dal.DBContext;
import model.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for audit_log table operations
 */
public class AuditLogDAO {
    private final DBContext dbContext;
    
    public AuditLogDAO() {
        this.dbContext = new DBContext();
    }
    
    /**
     * Retrieves all audit logs from the database with optional sorting
     * @param sortOrder "asc" for ascending (oldest first), "desc" for descending (newest first)
     * @return List of AuditLog objects
     * @throws SQLException if a database error occurs
     */
    public List<AuditLog> getAllAuditLogs(String sortOrder) throws SQLException {
        List<AuditLog> auditLogs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbContext.getConnection();
            
            // Check if audit_log table exists, if not create it
            try {
                String checkTableSql = "SELECT 1 FROM audit_log LIMIT 1";
                PreparedStatement checkStmt = conn.prepareStatement(checkTableSql);
                checkStmt.executeQuery();
                checkStmt.close();
            } catch (SQLException e) {
                // Table doesn't exist, create it
                String createTableSql = "CREATE TABLE IF NOT EXISTS `audit_log` (" +
                    "`id` INT AUTO_INCREMENT PRIMARY KEY, " +
                    "`action` VARCHAR(255) NOT NULL, " +
                    "`user_id` INT NOT NULL, " +
                    "`timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "`details` TEXT, " +
                    "FOREIGN KEY (user_id) REFERENCES Users(UserId))";
                
                PreparedStatement createStmt = conn.prepareStatement(createTableSql);
                createStmt.executeUpdate();
                createStmt.close();
                
                // Insert sample data
                String insertSql = "INSERT INTO `audit_log` (`action`, `user_id`, `details`) VALUES " +
                    "('Login', 1, 'User logged in successfully'), " +
                    "('Update Material', 1, 'Updated material ID 5'), " +
                    "('Create Material', 1, 'Created new material')";
                
                try {
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.executeUpdate();
                    insertStmt.close();
                } catch (SQLException ex) {
                    // Ignore if insert fails (e.g., if user_id 1 doesn't exist)
                }
            }
            
            String sql = "SELECT al.*, u.FullName AS user_name FROM audit_log al "
                    + "LEFT JOIN Users u ON al.user_id = u.UserId "
                    + "ORDER BY al.timestamp " + ("asc".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC");
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setId(rs.getInt("id"));
                log.setAction(rs.getString("action"));
                log.setUserId(rs.getInt("user_id"));
                log.setTimestamp(rs.getTimestamp("timestamp"));
                log.setUserName(rs.getString("user_name"));
                
                auditLogs.add(log);
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
        
        return auditLogs;
    }
    
    /**
     * Adds a new audit log entry
     * @param action The action performed
     * @param userId The ID of the user who performed the action
     * @param details Additional details about the action
     * @return true if the operation was successful
     * @throws SQLException if a database error occurs
     */
    public boolean addAuditLog(String action, int userId, String details) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dbContext.getConnection();
            
            String sql = "INSERT INTO audit_log (action, user_id, details) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, action);
            stmt.setInt(2, userId);
            stmt.setString(3, details);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { /* ignored */ }
            }
            // Note: We don't close the connection as it's managed by DBContext
        }
    }
}
