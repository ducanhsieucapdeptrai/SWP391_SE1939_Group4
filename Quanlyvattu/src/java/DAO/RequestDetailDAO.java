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

        String sql = """
        SELECT 
            rd.RequestId,
            rd.MaterialId,
            rd.Quantity,
            m.MaterialName,
            m.Image,
            m.Description,
            sc.SubCategoryName,
            c.CategoryName
        FROM RequestDetail rd
        JOIN Materials m ON rd.MaterialId = m.MaterialId
        LEFT JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
        LEFT JOIN Categories c ON sc.CategoryId = c.CategoryId
        WHERE rd.RequestId = ?
    """;

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(rs.getInt("RequestId"));
                detail.setMaterialId(rs.getInt("MaterialId"));
                detail.setQuantity(rs.getInt("Quantity"));

                detail.setMaterialName(rs.getString("MaterialName"));
                detail.setImage(rs.getString("Image"));
                detail.setDescription(rs.getString("Description"));
                detail.setSubCategoryName(rs.getString("SubCategoryName"));
                detail.setCategoryName(rs.getString("CategoryName"));

                // Nếu bạn không có giá thì không setPrice
                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public RequestList getRequestById(int requestId) {
        String sql = """
            SELECT r.RequestId, r.RequestedBy, u.FullName, r.RequestTypeId, rt.RequestTypeName,
                   r.RequestDate, r.Status, r.Note, r.ApprovedBy, r.ApprovedDate, r.ApprovalNote,
                   u2.FullName AS ApprovedByName
            FROM RequestList r
            JOIN Users u ON r.RequestedBy = u.UserId
            JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
            LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId
            WHERE r.RequestId = ?
        """;

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
                    req.setApprovedByName(rs.getString("ApprovedByName"));
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
        String sql = "SELECT Status FROM RequestList WHERE RequestId = ?";
        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Status");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getRequestStatus: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void approveRequest(int requestId, String note, int approvedBy, List<RequestDetail> newDetails) {
        try (Connection conn = new DBContext().getNewConnection()) {
            conn.setAutoCommit(false);

            String updateRequest = """
                UPDATE RequestList
                SET Status = 'Approved',
                    ApprovalNote = ?,
                    ApprovedBy = ?,
                    ApprovedDate = CURRENT_TIMESTAMP
                WHERE RequestId = ?
            """;

            try (PreparedStatement ps = conn.prepareStatement(updateRequest)) {
                ps.setString(1, note);
                ps.setInt(2, approvedBy);
                ps.setInt(3, requestId);
                ps.executeUpdate();
            }

            String checkSql = "SELECT COUNT(*) FROM RequestDetail WHERE RequestId = ? AND MaterialId = ?";
            String insertSql = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";
            String updateSql = "UPDATE RequestDetail SET Quantity = ? WHERE RequestId = ? AND MaterialId = ?";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                 PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

                for (RequestDetail detail : newDetails) {
                    checkStmt.setInt(1, requestId);
                    checkStmt.setInt(2, detail.getMaterialId());

                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            // Insert nếu chưa có
                            insertStmt.setInt(1, requestId);
                            insertStmt.setInt(2, detail.getMaterialId());
                            insertStmt.setInt(3, detail.getQuantity());
                            insertStmt.addBatch();
                        } else {
                            // Update nếu đã có
                            updateStmt.setInt(1, detail.getQuantity());
                            updateStmt.setInt(2, requestId);
                            updateStmt.setInt(3, detail.getMaterialId());
                            updateStmt.addBatch();
                        }
                    }
                }
                insertStmt.executeBatch();
                updateStmt.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            System.out.println("Error in approveRequest: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void rejectRequest(int requestId, String reason, int approvedBy) {
        String sql = """
            UPDATE RequestList
            SET Status = 'Rejected',
                ApprovalNote = ?,
                ApprovedBy = ?,
                ApprovedDate = CURRENT_TIMESTAMP
            WHERE RequestId = ?
        """;

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setInt(2, approvedBy);
            ps.setInt(3, requestId);
            int rows = ps.executeUpdate();
            System.out.println("Reject update result: " + rows + " rows affected.");
        } catch (Exception e) {
            System.out.println("Error in rejectRequest: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean checkMaterialInRequest(int requestId, int materialId) {
        String sql = "SELECT COUNT(*) FROM RequestDetail WHERE RequestId = ? AND MaterialId = ?";
        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.setInt(2, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in checkMaterialInRequest: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void insertMaterialToRequest(int requestId, int materialId, int quantity) {
        String sql = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";
        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.setInt(2, materialId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in insertMaterialToRequest: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
