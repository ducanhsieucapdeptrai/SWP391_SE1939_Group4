package DAO;

import dal.DBContext;
import model.RepairOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairOrderDAO extends DBContext {

    public List<RepairOrder> getRepairOrders(String createdByName, String status, String createdDate, int page, int pageSize) {
        List<RepairOrder> list = new ArrayList<>();
        String sql = """
            SELECT r.ROId, r.RequestId, u.FullName AS CreatedByName, r.CreatedDate, r.TotalPrice, r.Status, r.Note
            FROM RepairOrderList r
            JOIN Users u ON r.CreatedBy = u.UserId
            WHERE (u.FullName LIKE ? OR ? = '')
              AND (r.Status = ? OR ? = '')
              AND (CAST(r.CreatedDate AS DATE) = ? OR ? = '')
            ORDER BY r.CreatedDate DESC
            LIMIT ? OFFSET ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + createdByName + "%");
            ps.setString(2, createdByName);
            ps.setString(3, status);
            ps.setString(4, status);
            ps.setString(5, createdDate);
            ps.setString(6, createdDate);
            ps.setInt(7, pageSize);
            ps.setInt(8, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RepairOrder ro = new RepairOrder(
                    rs.getInt("ROId"),
                    rs.getInt("RequestId"),
                    rs.getString("CreatedByName"),
                    rs.getTimestamp("CreatedDate"),
                    rs.getDouble("TotalPrice"),
                    rs.getString("Status"),
                    rs.getString("Note")
                );
                list.add(ro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countRepairOrders(String createdByName, String status, String createdDate) {
        String sql = """
            SELECT COUNT(*) FROM RepairOrderList r
            JOIN Users u ON r.CreatedBy = u.UserId
            WHERE (u.FullName LIKE ? OR ? = '')
              AND (r.Status = ? OR ? = '')
              AND (CAST(r.CreatedDate AS DATE) = ? OR ? = '')
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + createdByName + "%");
            ps.setString(2, createdByName);
            ps.setString(3, status);
            ps.setString(4, status);
            ps.setString(5, createdDate);
            ps.setString(6, createdDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Create new Repair Order and details
    public boolean createRepairOrder(int userId, String note, String[] materialIds, String[] deviceStatus, String[] proposals, String[] budgets) {
    Connection conn = null;
    PreparedStatement psOrder = null;
    PreparedStatement psDetail = null;
    ResultSet rs = null;
    try {
        conn = connection;
        conn.setAutoCommit(false);

        // Insert order
        String sqlOrder = "INSERT INTO RepairOrderList (CreatedBy, CreatedDate, Note, Status) VALUES (?, NOW(), ?, 'Pending')";
        psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
        psOrder.setInt(1, userId);
        psOrder.setString(2, note);
        psOrder.executeUpdate();
        rs = psOrder.getGeneratedKeys();
        int roId = -1;
        if (rs.next()) roId = rs.getInt(1);
        else throw new SQLException("No ROId generated.");

        // Insert detail
        String sqlDetail = "INSERT INTO RepairOrderDetail (ROId, MaterialId, Quantity, UnitPrice, Total) VALUES (?, ?, ?, ?, ?)";
        psDetail = conn.prepareStatement(sqlDetail);
        for (int i = 0; i < materialIds.length; i++) {
            int materialId = Integer.parseInt(materialIds[i]);
            int quantity = 1; // default quantity
            double unitPrice = parseDoubleSafe(budgets[i]);
            double total = unitPrice * quantity;

            psDetail.setInt(1, roId);
            psDetail.setInt(2, materialId);
            psDetail.setInt(3, quantity);
            psDetail.setDouble(4, unitPrice);
            psDetail.setDouble(5, total);
            psDetail.addBatch();
        }
        psDetail.executeBatch();
        conn.commit();
        return true;
    } catch (Exception ex) {
        ex.printStackTrace();
        try { if (conn != null) conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
        return false;
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (psOrder != null) psOrder.close(); } catch (Exception e) {}
        try { if (psDetail != null) psDetail.close(); } catch (Exception e) {}
        try { if (conn != null) conn.setAutoCommit(true); } catch (Exception e) {}
    }
}


    // Helper: get materialId by material name
    private int getMaterialIdByName(String name) throws SQLException {
        String sql = "SELECT MaterialId FROM Materials WHERE MaterialName = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("MaterialId");
        }
        throw new SQLException("Material not found: " + name);
    }

    // Helper: parse double safely
    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }
}

