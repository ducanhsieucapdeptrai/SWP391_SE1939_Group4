package DAO;

import dal.DBContext;
import model.PasswordResetRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordResetDAO extends DBContext {

    public List<PasswordResetRequest> getAllPendingRequests() {
        List<PasswordResetRequest> list = new ArrayList<>();
        String sql = "SELECT r.RequestId, r.UserId, u.FullName, u.Email, u.Phone, r.RequestedAt "
                + "FROM PasswordResetRequest r "
                + "JOIN Users u ON r.UserId = u.UserId "
                + "WHERE r.Status = 'Pending' ORDER BY r.RequestedAt DESC";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PasswordResetRequest pr = new PasswordResetRequest();
                pr.setRequestId(rs.getInt("RequestId"));
                pr.setUserId(rs.getInt("UserId"));
                pr.setFullName(rs.getString("FullName"));
                pr.setEmail(rs.getString("Email"));
                pr.setPhone(rs.getString("Phone"));
                pr.setRequestedAt(rs.getTimestamp("RequestedAt"));
                list.add(pr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertPasswordResetRequest(int userId) {
        String checkSql = "SELECT COUNT(*) FROM PasswordResetRequest WHERE UserId = ? AND Status = 'Pending'";
        String insertSql = "INSERT INTO PasswordResetRequest (UserId, RequestedAt, Status) VALUES (?, NOW(), 'Pending')";

        try (Connection conn = getNewConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql); PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                checkStmt.setInt(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }

                insertStmt.setInt(1, userId);
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markAsProcessed(int requestId) {
        String sql = "UPDATE PasswordResetRequest SET Status = 'Processed' WHERE RequestId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
