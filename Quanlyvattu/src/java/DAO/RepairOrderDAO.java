package DAO;

import dal.DBContext;
import model.RepairOrderDetail;
import model.RepairOrderList;
import model.RequestList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepairOrderDAO extends DBContext {

    public List<RepairOrderDetail> getRepairPreviewByRequest(int requestId) {
        List<RepairOrderDetail> list = new ArrayList<>();
        String sql = """
        SELECT m.MaterialId, m.MaterialName, m.Price AS UnitPrice, rd.Quantity
        FROM RequestDetail rd
        JOIN Materials m ON rd.MaterialId = m.MaterialId
        WHERE rd.RequestId = ?
        """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RepairOrderDetail d = new RepairOrderDetail();
                    d.setMaterialId(rs.getInt("MaterialId"));
                    d.setMaterialName(rs.getString("MaterialName"));
                    d.setUnitPrice(rs.getDouble("UnitPrice"));
                    d.setQuantity(rs.getInt("Quantity"));
                    list.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int insertRepairOrderWithStatus(int requestId, int createdBy, String note, String status) {
        String sql = "INSERT INTO RepairOrderList (RequestId, CreatedBy, Note, Status) VALUES (?, ?, ?, ?)";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, requestId);
            ps.setInt(2, createdBy);
            ps.setString(3, note != null ? note : "");
            ps.setString(4, status);
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

    public void insertRepairDetail(int roId, RepairOrderDetail detail) {
        String sql = "INSERT INTO RepairOrderDetail (ROId, MaterialId, Quantity, UnitPrice, MNote) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roId);
            ps.setInt(2, detail.getMaterialId());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getUnitPrice());
            ps.setString(5, detail.getMnote()); // ✅ THÊM DÒNG NÀY
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existsByRequestId(int requestId) {
        String sql = "SELECT 1 FROM RepairOrderList WHERE RequestId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<RepairOrderList> getFilteredRepairOrdersPaged(String status, String createdDate, String createdByName, int offset, int pageSize) {
        List<RepairOrderList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT ro.*, 
               u1.FullName AS CreatedByName, 
               u2.FullName AS ApprovedByName
        FROM RepairOrderList ro
        JOIN Users u1 ON ro.CreatedBy = u1.UserId
        LEFT JOIN Users u2 ON ro.ApprovedBy = u2.UserId
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql.append(" AND ro.Status = ? ");
            params.add(status);
        }
        if (createdDate != null && !createdDate.isEmpty()) {
            sql.append(" AND DATE(ro.CreatedDate) = ? ");
            params.add(java.sql.Date.valueOf(createdDate));
        }
        if (createdByName != null && !createdByName.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ? ");
            params.add("%" + createdByName + "%");
        }

        sql.append(" ORDER BY ro.CreatedDate DESC LIMIT ? OFFSET ? ");
        params.add(pageSize);
        params.add(offset);

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RepairOrderList ro = new RepairOrderList();
                    ro.setRoId(rs.getInt("ROId"));
                    ro.setRequestId(rs.getInt("RequestId"));
                    ro.setCreatedBy(rs.getInt("CreatedBy"));
                    ro.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    ro.setStatus(rs.getString("Status"));

                    int approvedBy = rs.getInt("ApprovedBy");
                    ro.setApprovedBy(rs.wasNull() ? null : approvedBy);

                    Timestamp approvedDate = rs.getTimestamp("ApprovedDate");
                    ro.setApprovedDate(approvedDate != null ? new Date(approvedDate.getTime()) : null);

                    ro.setNote(rs.getString("Note"));
                    ro.setCreatedByName(rs.getString("CreatedByName"));
                    ro.setApprovedByName(rs.getString("ApprovedByName"));

                    // ✅ THÊM DÒNG NÀY để load chi tiết RepairOrderDetail
                    ro.setDetails(getRepairDetails(ro.getRoId()));

                    list.add(ro);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countFilteredRepairOrders(String status, String createdDate, String createdByName) {
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) 
        FROM RepairOrderList ro
        JOIN Users u1 ON ro.CreatedBy = u1.UserId
        WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql.append(" AND ro.Status = ? ");
            params.add(status);
        }
        if (createdDate != null && !createdDate.isEmpty()) {
            sql.append(" AND DATE(ro.CreatedDate) = ? ");
            params.add(java.sql.Date.valueOf(createdDate));
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

    public RepairOrderList getRepairOrderById(int roId) {
        String sql = """
        SELECT ro.ROId, ro.RequestId, ro.CreatedBy, u1.FullName AS CreatedByName,
               ro.CreatedDate, ro.Status, ro.Note,
               ro.ApprovedBy, ro.ApprovedDate, u2.FullName AS ApprovedByName
        FROM RepairOrderList ro
        JOIN Users u1 ON ro.CreatedBy = u1.UserId
        LEFT JOIN Users u2 ON ro.ApprovedBy = u2.UserId
        WHERE ro.ROId = ?
    """;

        RepairOrderList ro = null;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ro = new RepairOrderList();
                    ro.setRoId(rs.getInt("ROId"));
                    ro.setRequestId(rs.getInt("RequestId"));
                    ro.setCreatedBy(rs.getInt("CreatedBy"));
                    ro.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    ro.setStatus(rs.getString("Status"));
                    ro.setNote(rs.getString("Note"));
                    ro.setCreatedByName(rs.getString("CreatedByName"));

                    // ✅ Thêm các trường ApprovedBy, ApprovedDate, ApprovedByName
                    int approvedBy = rs.getInt("ApprovedBy");
                    ro.setApprovedBy(rs.wasNull() ? null : approvedBy);

                    Timestamp approvedDate = rs.getTimestamp("ApprovedDate");
                    ro.setApprovedDate(approvedDate != null ? new Date(approvedDate.getTime()) : null);

                    ro.setApprovedByName(rs.getString("ApprovedByName"));

                    // ✅ Load danh sách vật tư
                    ro.setDetails(getRepairDetails(roId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ro;
    }

    public List<RepairOrderDetail> getRepairDetails(int roId) {
        List<RepairOrderDetail> list = new ArrayList<>();
        String sql = """
SELECT d.ROId, d.MaterialId, m.MaterialName, d.Quantity, d.UnitPrice, d.MNote
FROM RepairOrderDetail d
JOIN Materials m ON d.MaterialId = m.MaterialId
WHERE d.ROId = ?
""";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RepairOrderDetail d = new RepairOrderDetail();
                    d.setRoId(rs.getInt("ROId"));
                    d.setMaterialId(rs.getInt("MaterialId"));
                    d.setMaterialName(rs.getString("MaterialName"));
                    d.setQuantity(rs.getInt("Quantity"));
                    d.setUnitPrice(rs.getDouble("UnitPrice"));
                    d.setMnote(rs.getString("MNote"));

                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateStatus(int roId, String status, int approvedBy) {
        String sql = """
            UPDATE RepairOrderList
            SET Status = ?, ApprovedBy = ?, ApprovedDate = CURRENT_TIMESTAMP
            WHERE ROId = ?
        """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, approvedBy);
            ps.setInt(3, roId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createRepairOrderFromRequest(RequestList requestInfo, List<RepairOrderDetail> detailList, StringBuilder log) {
        log.append("🟢 DAO.createRepairOrderFromRequest CALLED<br/>");

        String insertRO = "INSERT INTO RepairOrderList (RequestId, CreatedBy, Note) VALUES (?, ?, ?)";
        String insertDetail = "INSERT INTO RepairOrderDetail (ROId, MaterialId, Quantity, UnitPrice, MNote) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement psRO = null;
        PreparedStatement psDetail = null;
        ResultSet rs = null;

        try {
            // ✅ LẤY CONNECTION MỚI
            conn = getNewConnection();
            conn.setAutoCommit(false);
            log.append("🔌 Connection opened via getNewConnection()<br/>");

            psRO = conn.prepareStatement(insertRO, Statement.RETURN_GENERATED_KEYS);
            psRO.setInt(1, requestInfo.getRequestId());
            psRO.setInt(2, requestInfo.getRequestedBy());
            psRO.setString(3, requestInfo.getNote() != null ? requestInfo.getNote() : "");

            int affected = psRO.executeUpdate();
            log.append("➡ Insert RepairOrderList affected rows: ").append(affected).append("<br/>");

            if (affected == 0) {
                log.append("❌ Failed to insert RepairOrderList<br/>");
                conn.rollback();
                return false;
            }

            rs = psRO.getGeneratedKeys();
            if (rs.next()) {
                int roId = rs.getInt(1);
                log.append("✅ Created ROId: ").append(roId).append("<br/>");

                psDetail = conn.prepareStatement(insertDetail);
                for (RepairOrderDetail detail : detailList) {
                    log.append("📦 Inserting Detail → MaterialId=").append(detail.getMaterialId())
                            .append(", Qty=").append(detail.getQuantity())
                            .append(", Price=").append(detail.getUnitPrice())
                            .append(", Note=").append(detail.getMnote()).append("<br/>");

                    psDetail.setInt(1, roId);
                    psDetail.setInt(2, detail.getMaterialId());
                    psDetail.setInt(3, detail.getQuantity());
                    psDetail.setDouble(4, detail.getUnitPrice());
                    psDetail.setString(5, detail.getMnote());
                    psDetail.addBatch();
                }

                psDetail.executeBatch();
                conn.commit();
                log.append("✅ Transaction committed<br/>");
                return true;
            } else {
                log.append("❌ No ROId returned from generated keys.<br/>");
                conn.rollback();
            }

        } catch (Exception ex) {
            log.append("❌ Exception: ").append(ex.getMessage()).append("<br/>");
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception e) {
                log.append("⚠ Rollback Exception: ").append(e.getMessage()).append("<br/>");
            }
        } finally {
            // Chỉ đóng ps và rs, KHÔNG đóng conn
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.append("⚠ rs close: ").append(e.getMessage()).append("<br/>");
            }
            try {
                if (psRO != null) {
                    psRO.close();
                }
            } catch (Exception e) {
                log.append("⚠ psRO close: ").append(e.getMessage()).append("<br/>");
            }
            try {
                if (psDetail != null) {
                    psDetail.close();
                }
            } catch (Exception e) {
                log.append("⚠ psDetail close: ").append(e.getMessage()).append("<br/>");
            }

            // ❌ KHÔNG đóng conn, KHÔNG reset autoCommit
            // if (conn != null) {
            //     conn.setAutoCommit(true);
            //     conn.close();
            // }
        }

        return false;
    }

}
