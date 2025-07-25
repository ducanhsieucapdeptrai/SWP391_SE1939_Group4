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
        SELECT m.MaterialId, m.MaterialName, rd.Quantity, u.Name AS UnitName
        FROM RequestDetail rd
        JOIN Materials m ON rd.MaterialId = m.MaterialId
        JOIN Units u ON m.Unit_id = u.Unit_id
        WHERE rd.RequestId = ?
    """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderDetail d = new PurchaseOrderDetail();
                    d.setMaterialId(rs.getInt("MaterialId"));
                    d.setMaterialName(rs.getString("MaterialName"));
                    d.setQuantity(rs.getInt("Quantity"));
                    d.setUnitPrice(0);
                    d.setTotal(0);
                    d.setUnitName(rs.getString("UnitName"));
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
            ps.setDouble(4, detail.getUnitPrice());
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

    public PurchaseOrderList getPurchaseOrderById(int poId) {
        String sql = """
        SELECT p.POId, p.RequestId, p.CreatedBy, u.FullName AS CreatedByName,
               p.CreatedDate, p.TotalPrice, p.Status, p.Note,
               rl.RequestedBy, u2.FullName AS RequestedByName
        FROM PurchaseOrderList p
        JOIN Users u ON p.CreatedBy = u.UserId
        JOIN RequestList rl ON p.RequestId = rl.RequestId
        JOIN Users u2 ON rl.RequestedBy = u2.UserId
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
//                    po.setRequestedBy(rs.getInt("RequestedBy"));
//                    po.setRequestedByName(rs.getString("RequestedByName"));
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
        SELECT d.POId, d.MaterialId, m.MaterialName, d.Quantity, d.UnitPrice, d.Total, u.Name AS UnitName
        FROM PurchaseOrderDetail d
        JOIN Materials m ON d.MaterialId = m.MaterialId
        JOIN Units u ON m.Unit_id = u.Unit_id
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
                    d.setUnitPrice(rs.getDouble("UnitPrice"));
                    d.setTotal(rs.getDouble("Total"));
                    d.setUnitName(rs.getString("UnitName"));
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
            boolean updated = ps.executeUpdate() > 0;

            // ✅ Nếu duyệt thì tạo mới Import Request
            if (updated && "Approved".equalsIgnoreCase(status)) {
                PurchaseOrderList po = getPurchaseOrderById(poId);
                if (po != null) {
                    po.setApprovedBy(approvedBy); // Gán để truyền vào insert
                    createImportRequestFromPO(po);
                }
            }
            return updated;
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

    public boolean createImportRequestFromPO(PurchaseOrderList po) {
        String insertRequestSql = """
        INSERT INTO RequestList (
            RequestedBy, RequestDate, RequestTypeId, SubTypeId, Note, 
            Status, ApprovedBy, ApprovedDate
        )
        VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, 'Approved', ?, CURRENT_TIMESTAMP)
    """;

        String insertDetailSql = """
        INSERT INTO RequestDetail (RequestId, MaterialId, Quantity, ActualQuantity)
        VALUES (?, ?, ?, 0)
    """;

        try (Connection conn = getNewConnection()) {
            conn.setAutoCommit(false);

            if (po == null || po.getDetails() == null || po.getDetails().isEmpty()) {
                return false;
            }

            int requestId = 0;
            try (PreparedStatement ps = conn.prepareStatement(insertRequestSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, po.getCreatedBy());     // RequestedBy
                ps.setInt(2, 2);                     // RequestTypeId = 2 (Import)
                ps.setInt(3, 1);                     // SubTypeId = 1 (New Purchase)
                ps.setString(4, po.getNote());       // Note
                ps.setInt(5, po.getApprovedBy());    // ApprovedBy

                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        requestId = rs.getInt(1);
                    }
                }
            }

            if (requestId == 0) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement psDetail = conn.prepareStatement(insertDetailSql)) {
                for (PurchaseOrderDetail d : po.getDetails()) {
                    psDetail.setInt(1, requestId);
                    psDetail.setInt(2, d.getMaterialId());
                    psDetail.setInt(3, d.getQuantity());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
