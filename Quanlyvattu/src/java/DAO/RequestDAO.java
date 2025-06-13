package DAO;

import dal.DBContext;
import model.RequestList;
import model.RequestType;
import model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Material;
import model.RequestDetail;

public class RequestDAO {

    public static List<RequestList> getPendingRequests() {
        List<RequestList> list = new ArrayList<>();

        // Sử dụng tên database cụ thể để tránh lỗi "No database selected"
        String sql = "SELECT rl.RequestId, rl.RequestTypeId, rt.RequestTypeName, u.FullName "
                + "FROM quan_ly_vat_tu.RequestList rl "
                + "JOIN quan_ly_vat_tu.RequestType rt ON rl.RequestTypeId = rt.RequestTypeId "
                + "JOIN quan_ly_vat_tu.Users u ON rl.RequestedBy = u.UserId "
                + "WHERE rl.Status = 'Pending'";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));

                Users user = new Users();
                user.setFullName(rs.getString("FullName"));
                r.setRequester(user);

                RequestType type = new RequestType();
                type.setRequestTypeId(rs.getInt("RequestTypeId"));
                type.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setRequestType(type);

                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static RequestList getRequestById(int id) {
        String sql = "SELECT rl.*, u.FullName, rt.RequestTypeName "
                + "FROM RequestList rl "
                + "JOIN Users u ON rl.RequestedBy = u.UserId "
                + "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId "
                + "WHERE rl.RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(id);
                r.setRequestDate(rs.getDate("RequestDate"));
                r.setNote(rs.getString("Note"));
                Users u = new Users();
                u.setFullName(rs.getString("FullName"));
                r.setRequester(u);
                RequestType rt = new RequestType();
                rt.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setRequestType(rt);
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<RequestDetail> getRequestDetails(int requestId) {
        List<RequestDetail> list = new ArrayList<>();
        String sql = "SELECT rd.Quantity, m.MaterialId, m.MaterialName "
                + "FROM RequestDetail rd "
                + "JOIN Materials m ON rd.MaterialId = m.MaterialId "
                + "WHERE rd.RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestDetail rd = new RequestDetail();
                rd.setQuantity(rs.getInt("Quantity"));
                rd.setMaterialId(rs.getInt("MaterialId"));

                Material m = new Material();
                m.setMaterialName(rs.getString("MaterialName"));
                m.setMaterialId(rs.getInt("MaterialId")); // optional, for flexibility
                rd.setMaterial(m);

                list.add(rd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void approveRequest(int requestId, int approverId) {
        String sql = "UPDATE RequestList SET Status = 'Approved', ApprovedBy = ?, ApprovedDate = NOW() WHERE RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, approverId);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rejectRequest(int requestId, int approverId, String note) {
        String sql = "UPDATE RequestList SET Status = 'Rejected', ApprovedBy = ?, ApprovedDate = NOW(), ApprovalNote = ? WHERE RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, approverId);
            ps.setString(2, note);
            ps.setInt(3, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testRequestDetails(int requestId) {
        List<RequestDetail> details = getRequestDetails(requestId);
        if (details.isEmpty()) {
            System.out.println("❌ Không tìm thấy vật tư nào cho Request ID = " + requestId);
        } else {
            System.out.println("✅ Danh sách vật tư cho Request ID = " + requestId + ":");
            for (RequestDetail d : details) {
                System.out.println(" - Material: " + d.getMaterial().getMaterialName()
                        + ", Quantity: " + d.getQuantity());
            }
        }
    }

    public static void main(String[] args) {
        int requestIdToTest = 1; // Bạn có thể thay bằng ID thật từ DB
        testRequestDetails(requestIdToTest);
        try {
            List<RequestList> pending = getPendingRequests();

            if (pending.isEmpty()) {
                System.out.println("✅ Kết nối thành công nhưng không có request đang chờ duyệt.");
            } else {
                System.out.println("✅ Kết nối thành công. Có " + pending.size() + " request đang chờ:");
                for (RequestList r : pending) {
                    System.out.println(" - Request ID: " + r.getRequestId()
                            + ", Requester: " + r.getRequester().getFullName()
                            + ", Type: " + r.getRequestType().getRequestTypeName());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi kết nối hoặc truy vấn: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
