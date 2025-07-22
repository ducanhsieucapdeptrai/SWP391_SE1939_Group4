package DAO;

import dal.DBContext;
import model.RepairOrderDetail;
import model.RepairOrderList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RODetailDAO extends DBContext {

    /**
     * Tạo mới một Export Request từ phiếu sửa chữa đã được duyệt.
     */
    public boolean createExportRequestFromRepairOrder(RepairOrderList ro) {
        System.out.println("⛳ Bắt đầu tạo Export Request từ RO #" + ro.getRoId());

        String insertRequestSQL = """
           INSERT INTO RequestList 
            (RequestedBy, RequestTypeId, SubTypeId, Note, Status, ApprovedBy, ApprovedDate, IsTransferredToday, IsUpdated)
            VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?)
        """;

        String insertDetailSQL = """
            INSERT INTO RequestDetail (RequestId, MaterialId, Quantity )
            VALUES (?, ?, ?)
        """;

        try (Connection conn = getNewConnection(); PreparedStatement stRequest = conn.prepareStatement(insertRequestSQL, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            int requestTypeId = 1; // Export
            int subTypeId = 2;     // For Equipment Repair

// Insert RequestList
            stRequest.setInt(1, ro.getCreatedBy());
            stRequest.setInt(2, requestTypeId); // ✅ FIXED: sử dụng biến đã khai báo
            stRequest.setInt(3, subTypeId);
            stRequest.setString(4, "Export request auto-created from Repair Order #" + ro.getRoId());
            stRequest.setString(5, "Approved");
            stRequest.setInt(6, ro.getApprovedBy());
            stRequest.setBoolean(7, false); // IsTransferredToday
            stRequest.setBoolean(8, false); // IsUpdated

            int affected = stRequest.executeUpdate();
            if (affected == 0) {
                System.err.println("❌ Không insert được RequestList.");
                conn.rollback();
                return false;
            }

            int newRequestId;
            try (ResultSet rs = stRequest.getGeneratedKeys()) {
                if (rs.next()) {
                    newRequestId = rs.getInt(1);
                    System.out.println("✅ Export RequestId mới: " + newRequestId);
                } else {
                    System.err.println("❌ Không lấy được RequestId mới.");
                    conn.rollback();
                    return false;
                }
            }

            // Insert RequestDetail
            try (PreparedStatement stDetail = conn.prepareStatement(insertDetailSQL)) {
                for (RepairOrderDetail d : ro.getDetails()) {
                    stDetail.setInt(1, newRequestId);
                    stDetail.setInt(2, d.getMaterialId());
                    stDetail.setInt(3, d.getQuantity());
                    stDetail.addBatch();

                    System.out.println("➡️ Material: " + d.getMaterialId() + ", Qty: " + d.getQuantity());
                }
                stDetail.executeBatch();
            }

            conn.commit();
            System.out.println("🎯 Xuất kho từ RO #" + ro.getRoId() + " hoàn tất.");
            return true;

        } catch (Exception e) {
            throw new RuntimeException("❌ Lỗi khi tạo Export Request: " + e.getMessage(), e);
        }

    }

    /**
     * Lấy SubTypeId cho một SubType cụ thể của Export.
     */
    private int getExportSubTypeId(Connection conn, String subTypeName) {
        String sql = """
            SELECT SubTypeId 
            FROM RequestSubType 
            WHERE RequestTypeId = 1 AND SubTypeName = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subTypeName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SubTypeId");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi truy vấn SubTypeId: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Truy vấn chi tiết vật tư của một Repair Order.
     */
    public List<RepairOrderDetail> getRepairDetailsByROId(int roId) {
        List<RepairOrderDetail> list = new ArrayList<>();
        String sql = """
        SELECT rod.ROId, rod.MaterialId, rod.Quantity, rod.UnitPrice, rod.MNote, m.MaterialName
        FROM RepairOrderDetail rod
        JOIN Materials m ON rod.MaterialId = m.MaterialId
        WHERE rod.ROId = ?
    """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RepairOrderDetail detail = new RepairOrderDetail();
                    detail.setRoId(rs.getInt("ROId"));
                    detail.setMaterialId(rs.getInt("MaterialId"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setUnitPrice(rs.getDouble("UnitPrice"));
                    detail.setMnote(rs.getString("MNote")); // ✅ Gán note
                    detail.setMaterialName(rs.getString("MaterialName"));
                    list.add(detail);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy chi tiết RO: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

}
