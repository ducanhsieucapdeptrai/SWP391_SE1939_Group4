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
     *
     * @param sortOrder "asc" for ascending (oldest first), "desc" for
     * descending (newest first)
     * @return List of AuditLog objects
     * @throws SQLException if a database error occurs
     */
    public List<AuditLog> getAllAuditLogs(String sortOrder) throws SQLException {
        List<AuditLog> auditLogs = new ArrayList<>();

        String sql = "SELECT al.*, u.FullName AS user_name FROM audit_log al "
                + "LEFT JOIN Users u ON al.user_id = u.UserId "
                + "ORDER BY al.timestamp " + ("asc".equalsIgnoreCase(sortOrder) ? "ASC" : "DESC");

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

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
        }

        return auditLogs;
    }

    /**
     * Adds a new audit log entry
     *
     * @param action The action performed
     * @param userId The ID of the user who performed the action
     * @param details Additional details about the action
     * @return true if the operation was successful
     * @throws SQLException if a database error occurs
     */
    public boolean addAuditLog(String action, int userId, String details) throws SQLException {
        String sql = "INSERT INTO audit_log (action, user_id, details) VALUES (?, ?, ?)";

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, action);
            stmt.setInt(2, userId);
            stmt.setString(3, details);

            return stmt.executeUpdate() > 0;
        }
    }
}
