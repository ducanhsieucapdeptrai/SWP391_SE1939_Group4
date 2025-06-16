package DAO;

import dal.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.PurchaseOrderList;

public class PurchaseOrderDAO {

    public static void createPurchaseOrder(int employeeId, int requestId, String note,
            String[] materialIds, String[] quantities) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            // 1. Tính tổng giá trị
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

            // 2. Insert vào PurchaseOrderList (có TotalPrice)
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

            // 3. Insert vào PurchaseOrderDetail
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

            // 4. Update Request status to 'Approved' (Fix: đúng tên bảng là RequestList)
            String updateStatus = "UPDATE RequestList SET Status = 'Approved' WHERE RequestId = ?";
            ps = conn.prepareStatement(updateStatus);
            ps.setInt(1, requestId);
            ps.executeUpdate();

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
        String query = "SELECT * FROM PurchaseOrderList WHERE Status = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PurchaseOrderList po = new PurchaseOrderList();
                po.setPoId(rs.getInt("POId"));
                po.setRequestId(rs.getInt("RequestId"));
                po.setCreatedBy(rs.getInt("CreatedBy"));
                po.setCreatedDate(rs.getTimestamp("CreatedDate"));
                po.setTotalPrice(rs.getDouble("TotalPrice"));
                po.setStatus(rs.getString("Status"));
                po.setNote(rs.getString("Note"));
                list.add(po);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        int employeeId = 12; // Thay bằng userId nhân viên công ty trong DB
        int requestId = 2;   // ID của một request đã được approve, loại "Material Purchase"
        String note = "Auto-generated PO test";

        // Giả lập dữ liệu từ requestDetail
        String[] materialIds = {"1", "3"};  // materialId hợp lệ trong DB
        String[] quantities = {"10", "5"};  // số lượng tương ứng

        createPurchaseOrder(employeeId, requestId, note, materialIds, quantities);
    }
}
