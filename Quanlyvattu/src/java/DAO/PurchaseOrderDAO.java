package DAO;

import dal.DBContext;
import model.PurchaseOrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.PurchaseOrderList;

public class PurchaseOrderDAO extends DBContext {

    public List<PurchaseOrderDetail> getPurchasePreviewByRequest(int requestId) {
        List<PurchaseOrderDetail> list = new ArrayList<>();
        String sql = """
            SELECT m.MaterialId, m.MaterialName, m.Price, rd.Quantity,
                   (m.Price * rd.Quantity) AS Total
            FROM RequestDetail rd
            JOIN Materials m ON rd.MaterialId = m.MaterialId
            WHERE rd.RequestId = ?
        """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderDetail d = new PurchaseOrderDetail();
                    d.setMaterialId(rs.getInt("MaterialId"));
                    d.setMaterialName(rs.getString("MaterialName"));
                    d.setPrice(rs.getDouble("Price"));
                    d.setQuantity(rs.getInt("Quantity"));
                    d.setTotal(rs.getDouble("Total"));
                    list.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int insertPurchaseOrderWithStatus(int requestId, int createdBy, double total, String note, String status) {
        String sql = "INSERT INTO PurchaseOrderList (RequestId, CreatedBy, TotalPrice, Note, Status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, requestId);
            ps.setInt(2, createdBy);
            ps.setDouble(3, total);
            ps.setString(4, note != null ? note : "");
            ps.setString(5, status);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void insertPODetail(int poId, PurchaseOrderDetail detail) {
        String sql = "INSERT INTO PurchaseOrderDetail (POId, MaterialId, Quantity, UnitPrice, Total) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poId);
            ps.setInt(2, detail.getMaterialId());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getPrice());
            ps.setDouble(5, detail.getTotal());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existsByRequestId(int requestId) {
        String sql = "SELECT 1 FROM PurchaseOrderList WHERE RequestId = ?";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<PurchaseOrderList> getFilteredPurchaseOrders(String status, String createdDate) {
        List<PurchaseOrderList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT po.*, 
               u1.FullName AS CreatedByName, 
               u2.FullName AS ApprovedByName
        FROM PurchaseOrderList po
        JOIN Users u1 ON po.CreatedBy = u1.UserId
        LEFT JOIN Users u2 ON po.ApprovedBy = u2.UserId
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql.append(" AND po.Status = ? ");
            params.add(status);
        }
        if (createdDate != null && !createdDate.isEmpty()) {
            sql.append(" AND DATE(po.CreatedDate) = ? ");
            params.add(Date.valueOf(createdDate));
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderList po = new PurchaseOrderList();
                    po.setPoId(rs.getInt("POId"));
                    po.setRequestId(rs.getInt("RequestId"));
                    po.setCreatedBy(rs.getInt("CreatedBy"));
                    po.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    po.setTotalPrice(rs.getDouble("TotalPrice"));
                    po.setStatus(rs.getString("Status"));

                    int approvedBy = rs.getInt("ApprovedBy");
                    po.setApprovedBy(rs.wasNull() ? null : approvedBy);

                    Timestamp approvedDate = rs.getTimestamp("ApprovedDate");
                    po.setApprovedDate(approvedDate != null ? new Date(approvedDate.getTime()) : null);

                    po.setNote(rs.getString("Note"));

                    // Gán tên từ JOIN users
                    po.setCreatedByName(rs.getString("CreatedByName"));
                    po.setApprovedByName(rs.getString("ApprovedByName"));

                    list.add(po);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public PurchaseOrderList getPurchaseOrderById(int poId) {
        String sql = """
        SELECT p.POId, p.RequestId, p.CreatedBy, u.FullName AS CreatedByName,
               p.CreatedDate, p.TotalPrice, p.Status, p.Note
        FROM PurchaseOrderList p
        JOIN Users u ON p.CreatedBy = u.UserId
        WHERE p.POId = ?
    """;
        PurchaseOrderList po = null;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, poId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    po = new PurchaseOrderList();
                    po.setPoId(rs.getInt("POId"));
                    po.setRequestId(rs.getInt("RequestId"));
                    po.setCreatedBy(rs.getInt("CreatedBy"));
                    po.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    po.setTotalPrice(rs.getDouble("TotalPrice"));
                    po.setStatus(rs.getString("Status"));
                    po.setNote(rs.getString("Note"));
                    po.setCreatedByName(rs.getString("CreatedByName"));

                    // Load chi tiết đơn hàng
                    po.setDetails(getPODetails(poId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return po;
    }

    public List<PurchaseOrderDetail> getPODetails(int poId) {
        List<PurchaseOrderDetail> list = new ArrayList<>();
        String sql = """
        SELECT d.POId, d.MaterialId, m.MaterialName, d.Quantity, d.UnitPrice, d.Total
        FROM PurchaseOrderDetail d
        JOIN Materials m ON d.MaterialId = m.MaterialId
        WHERE d.POId = ?
    """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, poId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderDetail d = new PurchaseOrderDetail();
                    d.setPoId(rs.getInt("POId"));
                    d.setMaterialId(rs.getInt("MaterialId"));
                    d.setMaterialName(rs.getString("MaterialName"));
                    d.setQuantity(rs.getInt("Quantity"));
                    d.setPrice(rs.getDouble("UnitPrice"));
                    d.setTotal(rs.getDouble("Total"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateStatus(int poId, String status, int approvedBy) {
        String sql = """
        UPDATE PurchaseOrderList
        SET Status = ?, ApprovedBy = ?, ApprovedDate = CURRENT_TIMESTAMP
        WHERE POId = ?
    """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, approvedBy);
            ps.setInt(3, poId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PurchaseOrderList> getFilteredPurchaseOrdersPaged(String status, String createdDate, String createdByName, int offset, int pageSize) {
        List<PurchaseOrderList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT po.*, 
               u1.FullName AS CreatedByName, 
               u2.FullName AS ApprovedByName
        FROM PurchaseOrderList po
        JOIN Users u1 ON po.CreatedBy = u1.UserId
        LEFT JOIN Users u2 ON po.ApprovedBy = u2.UserId
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql.append(" AND po.Status = ? ");
            params.add(status);
        }
        if (createdDate != null && !createdDate.isEmpty()) {
            sql.append(" AND DATE(po.CreatedDate) = ? ");
            params.add(Date.valueOf(createdDate));
        }
        if (createdByName != null && !createdByName.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ? ");
            params.add("%" + createdByName + "%");
        }

        sql.append(" ORDER BY po.CreatedDate DESC LIMIT ? OFFSET ? ");
        params.add(pageSize);
        params.add(offset);

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderList po = new PurchaseOrderList();
                    po.setPoId(rs.getInt("POId"));
                    po.setRequestId(rs.getInt("RequestId"));
                    po.setCreatedBy(rs.getInt("CreatedBy"));
                    po.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    po.setTotalPrice(rs.getDouble("TotalPrice"));
                    po.setStatus(rs.getString("Status"));

                    int approvedBy = rs.getInt("ApprovedBy");
                    po.setApprovedBy(rs.wasNull() ? null : approvedBy);

                    Timestamp approvedDate = rs.getTimestamp("ApprovedDate");
                    po.setApprovedDate(approvedDate != null ? new Date(approvedDate.getTime()) : null);

                    po.setNote(rs.getString("Note"));
                    po.setCreatedByName(rs.getString("CreatedByName"));
                    po.setApprovedByName(rs.getString("ApprovedByName"));

                    list.add(po);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countFilteredPurchaseOrders(String status, String createdDate, String createdByName) {
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) 
        FROM PurchaseOrderList po
        JOIN Users u1 ON po.CreatedBy = u1.UserId
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql.append(" AND po.Status = ? ");
            params.add(status);
        }
        if (createdDate != null && !createdDate.isEmpty()) {
            sql.append(" AND DATE(po.CreatedDate) = ? ");
            params.add(Date.valueOf(createdDate));
        }
        if (createdByName != null && !createdByName.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ? ");
            params.add("%" + createdByName + "%");
        }

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
