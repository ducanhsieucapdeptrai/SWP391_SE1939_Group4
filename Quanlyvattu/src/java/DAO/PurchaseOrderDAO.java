package DAO;

import dal.DBContext;
import model.PurchaseOrderList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAO {

    public static void createPurchaseOrder(int employeeId, int requestId, String note,
            String[] materialIds, String[] quantities) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            double totalPrice = 0;
            double[] unitPrices = new double[materialIds.length];
            double[] totals = new double[materialIds.length];

            for (int i = 0; i < materialIds.length; i++) {
                int materialId = Integer.parseInt(materialIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                double price = MaterialDAO.getMaterialPrice(materialId);
                double total = price * quantity;

                unitPrices[i] = price;
                totals[i] = total;
                totalPrice += total;
            }

            String insertPO = "INSERT INTO PurchaseOrderList (RequestId, CreatedBy, Note, TotalPrice) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(insertPO, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, requestId);
            ps.setInt(2, employeeId);
            ps.setString(3, note);
            ps.setDouble(4, totalPrice);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int purchaseOrderId = 0;
            if (rs.next()) {
                purchaseOrderId = rs.getInt(1);
            }
            ps.close();

            String insertDetail = "INSERT INTO PurchaseOrderDetail (POId, MaterialId, Quantity, UnitPrice, Total) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertDetail);

            for (int i = 0; i < materialIds.length; i++) {
                int materialId = Integer.parseInt(materialIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                ps.setInt(1, purchaseOrderId);
                ps.setInt(2, materialId);
                ps.setInt(3, quantity);
                ps.setDouble(4, unitPrices[i]);
                ps.setDouble(5, totals[i]);
                ps.addBatch();
            }

            ps.executeBatch();

            String insertStatus = "INSERT INTO PurchaseOrderStatus (POId, Status) VALUES (?, 'Pending')";
            PreparedStatement psStatus = conn.prepareStatement(insertStatus);
            psStatus.setInt(1, purchaseOrderId);
            psStatus.executeUpdate();

            conn.commit();
            System.out.println("✅ Purchase Order created successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static List<PurchaseOrderList> getPurchaseOrdersByStatus(String status) {
        List<PurchaseOrderList> list = new ArrayList<>();

        String sql = "SELECT po.*, u.FullName AS CreatedByName "
                + "FROM PurchaseOrderList po "
                + "JOIN users u ON po.CreatedBy = u.UserId "
                + "WHERE po.Status = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status); // ✅ gán giá trị vào dấu hỏi

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderList po = new PurchaseOrderList();
                    po.setPoId(rs.getInt("POId"));
                    po.setRequestId(rs.getInt("RequestId"));
                    po.setCreatedBy(rs.getInt("CreatedBy"));
                    po.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    po.setTotalPrice(rs.getDouble("TotalPrice"));
                    po.setStatus(rs.getString("Status"));
                    po.setNote(rs.getString("Note"));
                    po.setCreatedByName(rs.getString("CreatedByName")); // ✅ gán tên người tạo

                    list.add(po);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static PurchaseOrderList getPurchaseOrderById(int poId) {
        PurchaseOrderList po = null;
        String sql = "SELECT po.*, u.FullName AS CreatedByName "
                + "FROM PurchaseOrderList po "
                + "JOIN users u ON po.CreatedBy = u.UserId "
                + "WHERE po.POId = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                po = new PurchaseOrderList();
                po.setPoId(rs.getInt("POId")); // ✅ Đúng tên cột
                po.setRequestId(rs.getInt("RequestId"));
                po.setCreatedBy(rs.getInt("CreatedBy"));
                po.setStatus(rs.getString("Status"));
                po.setCreatedDate(rs.getTimestamp("CreatedDate"));
                po.setTotalPrice(rs.getDouble("TotalPrice"));
                po.setApprovedBy(rs.getObject("ApprovedBy") != null ? rs.getInt("ApprovedBy") : null);
                po.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                po.setNote(rs.getString("Note"));
                po.setCreatedByName(rs.getString("CreatedByName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return po;
    }

    public static List<PurchaseOrderList> getPurchaseOrdersByStatus(String status, int page, int pageSize) {
        List<PurchaseOrderList> list = new ArrayList<>();
        String sql = """
        SELECT po.*, u.FullName AS CreatedByName
        FROM PurchaseOrderList po
        JOIN users u ON po.CreatedBy = u.UserId
        WHERE po.Status = ?
        ORDER BY po.POId DESC
        LIMIT ? OFFSET ?""";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseOrderList po = new PurchaseOrderList();
                po.setPoId(rs.getInt("POId"));
                po.setRequestId(rs.getInt("RequestId"));
                po.setCreatedBy(rs.getInt("CreatedBy"));
                po.setCreatedDate(rs.getTimestamp("CreatedDate"));
                po.setTotalPrice(rs.getDouble("TotalPrice"));
                po.setStatus(rs.getString("Status"));
                po.setApprovedBy(rs.getInt("ApprovedBy"));
                po.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                po.setNote(rs.getString("Note"));
                po.setCreatedByName(rs.getString("CreatedByName"));
                list.add(po);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static int countPurchaseOrdersByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM PurchaseOrderList WHERE Status = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void approvePurchaseOrder(int poId, int approverId, String note, Timestamp approvedDate) {
        String sql = "UPDATE PurchaseOrderList SET Status = 'Approved', ApprovedBy = ?, Note = ?, ApprovedDate = ? WHERE POId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, approverId);
            ps.setString(2, note);
            ps.setTimestamp(3, approvedDate);
            ps.setInt(4, poId);
            ps.executeUpdate();

            // Cập nhật bảng PurchaseOrderStatus
            String statusSql = "UPDATE PurchaseOrderStatus SET Status = 'Approved' WHERE POId = ?";
            try (PreparedStatement psStatus = conn.prepareStatement(statusSql)) {
                psStatus.setInt(1, poId);
                psStatus.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rejectPurchaseOrder(int poId, int approverId, String note, String approvedDate) {
        String sql = "UPDATE PurchaseOrderList SET Status = 'Rejected', ApprovedBy = ?, Note = ?, ApprovedDate = ? WHERE POId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, approverId);
            ps.setString(2, note);
            ps.setString(3, approvedDate); // Sử dụng setString thay vì setTimestamp
            ps.setInt(4, poId);

            ps.executeUpdate();

            // Cập nhật bảng status
            String statusSql = "UPDATE PurchaseOrderStatus SET Status = 'Rejected' WHERE POId = ?";
            try (PreparedStatement psStatus = conn.prepareStatement(statusSql)) {
                psStatus.setInt(1, poId);
                psStatus.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to reject PO", e);
        }
    }

    public static boolean checkIfRequestHasPO(int requestId) {
        String sql = "SELECT COUNT(*) FROM PurchaseOrderList WHERE RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getPOStatusByRequestId(int requestId) {
        String sql = "SELECT pos.Status FROM PurchaseOrderList pol "
                + "JOIN PurchaseOrderStatus pos ON pol.POId = pos.POId "
                + "WHERE pol.RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    public static int getPOIdByRequestId(int requestId) {
        String sql = "SELECT POId FROM PurchaseOrderList WHERE RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("POId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
