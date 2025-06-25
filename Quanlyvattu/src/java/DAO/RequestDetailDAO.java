package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.RequestDetail;
import model.RequestList;

public class RequestDetailDAO {

    public List<RequestDetail> getRequestDetailsByRequestId(int requestId) {
        List<RequestDetail> list = new ArrayList<>();

        String sql = "SELECT rd.RequestId, rd.MaterialId, rd.Quantity, "
                + "m.MaterialName, m.Price, m.Image, m.Description, "
                + "sc.SubCategoryName, c.CategoryName "
                + "FROM RequestDetail rd "
                + "JOIN Materials m ON rd.MaterialId = m.MaterialId "
                + "JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId "
                + "JOIN Categories c ON sc.CategoryId = c.CategoryId "
                + "WHERE rd.RequestId = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestDetail detail = new RequestDetail(
                        rs.getInt("RequestId"),
                        rs.getInt("MaterialId"),
                        rs.getInt("Quantity"),
                        rs.getString("MaterialName"),
                        rs.getDouble("Price"),
                        rs.getString("Image"),
                        rs.getString("Description"),
                        rs.getString("SubCategoryName"),
                        rs.getString("CategoryName")
                );
                list.add(detail);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error in getRequestDetailsByRequestId: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public RequestList getRequestById(int requestId) {
        String sql = "SELECT r.RequestId, r.RequestedBy, u.FullName, r.RequestTypeId, rt.RequestTypeName, "
                + "r.RequestDate, r.Status, r.Note, r.ApprovedBy, r.ApprovedDate, r.ApprovalNote "
                + "FROM RequestList r "
                + "JOIN Users u ON r.RequestedBy = u.UserId "
                + "JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId "
                + "WHERE r.RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RequestList req = new RequestList();
                    req.setRequestId(rs.getInt("RequestId"));
                    req.setRequestedBy(rs.getInt("RequestedBy"));
                    req.setRequestedByName(rs.getString("FullName"));
                    req.setRequestTypeId(rs.getInt("RequestTypeId"));
                    req.setRequestTypeName(rs.getString("RequestTypeName"));
                    req.setRequestDate(rs.getTimestamp("RequestDate"));
                    req.setStatus(rs.getString("Status"));
                    req.setNote(rs.getString("Note"));
                    req.setApprovedBy(rs.getInt("ApprovedBy"));
                    req.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                    req.setApprovalNote(rs.getString("ApprovalNote"));
                    return req;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRequestById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getRequestStatus(int requestId) {
        String sql = "SELECT Status FROM requestlist WHERE RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Status");
            }
        } catch (Exception e) {
            System.out.println("Error in getRequestStatus: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void approveRequest(int requestId, String note, int approvedBy, List<RequestDetail> newDetails) {
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);

            String updateRequest = "UPDATE requestlist SET Status = 'Approved', ApprovalNote = ?, ApprovedBy = ?, ApprovedDate = CURRENT_TIMESTAMP WHERE RequestId = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateRequest)) {
                ps.setString(1, note);
                ps.setInt(2, approvedBy);
                ps.setInt(3, requestId);
                ps.executeUpdate();
            }

            String checkSql = "SELECT COUNT(*) FROM RequestDetail WHERE RequestId = ? AND MaterialId = ?";
            String insertSql = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql); PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (RequestDetail detail : newDetails) {
                    checkStmt.setInt(1, requestId);
                    checkStmt.setInt(2, detail.getMaterialId());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        insertStmt.setInt(1, requestId);
                        insertStmt.setInt(2, detail.getMaterialId());
                        insertStmt.setInt(3, detail.getQuantity());
                        insertStmt.addBatch();
                    }
                    rs.close();
                }
                insertStmt.executeBatch();
            }

            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void rejectRequest(int requestId, String reason, int approvedBy) {
        String sql = "UPDATE requestlist SET Status = ?, RejectionReason = ?, ApprovedBy = ?, ApprovedDate = CURRENT_TIMESTAMP WHERE RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "Rejected");
            ps.setString(2, reason);
            ps.setInt(3, approvedBy);
            ps.setInt(4, requestId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in rejectRequest: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean checkMaterialInRequest(int requestId, int materialId) {
        String sql = "SELECT COUNT(*) FROM RequestDetail WHERE RequestId = ? AND MaterialId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.setInt(2, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertMaterialToRequest(int requestId, int materialId, int quantity) {
        String sql = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.setInt(2, materialId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
