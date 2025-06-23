package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.RequestList;
import model.RequestDetail;
import dal.DBContext;
import model.*;

public class RequestDAO extends DBContext {

    // Thêm method mới để lấy thông tin chi tiết của 1 request
    public RequestList getRequestById(int requestId) {
        RequestList request = null;
        String sql = "SELECT r.RequestId, r.RequestDate, r.Note, r.Status, "
                + "rt.RequestTypeName, rs.Description AS StatusDescription, "
                + "u1.FullName AS RequestedByName, "
                + "u2.FullName AS ApprovedByName, r.ApprovedDate, r.ApprovalNote, "
                + "it.ImportTypeName, et.ExportTypeName "
                + "FROM RequestList r "
                + "JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId "
                + "JOIN RequestStatus rs ON r.Status = rs.StatusCode "
                + "JOIN Users u1 ON r.RequestedBy = u1.UserId "
                + "LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId "
                + "LEFT JOIN ImportList il ON r.RequestId = il.RequestId "
                + "LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId "
                + "LEFT JOIN ExportList el ON r.RequestId = el.RequestId "
                + "LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId "
                + "WHERE r.RequestId = ?";

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, requestId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request = new RequestList();
                    request.setRequestId(rs.getInt("RequestId"));
                    request.setRequestDate(rs.getTimestamp("RequestDate"));
                    request.setNote(rs.getString("Note"));
                    request.setStatus(rs.getString("Status"));
                    request.setRequestTypeName(rs.getString("RequestTypeName"));
                    request.setStatusDescription(rs.getString("StatusDescription"));
                    request.setRequestedByName(rs.getString("RequestedByName"));
                    request.setApprovedByName(rs.getString("ApprovedByName"));
                    request.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                    request.setApprovalNote(rs.getString("ApprovalNote"));

                    // Set import/export type name if exists
                    String importType = rs.getString("ImportTypeName");
                    String exportType = rs.getString("ExportTypeName");
                    if (importType != null) {
                        request.setImportTypeName(importType);
                    }
                    if (exportType != null) {
                        request.setExportTypeName(exportType);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return request;
    }

    public List<RequestList> getAllRequests() {
        List<RequestList> list = new ArrayList<>();
        String sql = "SELECT r.RequestId, r.RequestDate, r.Note, "
                + "rt.RequestTypeName, rs.Description AS StatusDescription, "
                + "u1.FullName AS RequestedByName, "
                + "u2.FullName AS ApprovedByName, r.ApprovedDate, r.ApprovalNote, "
                + "it.ImportTypeName, et.ExportTypeName "
                + "FROM RequestList r "
                + "JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId "
                + "JOIN RequestStatus rs ON r.Status = rs.StatusCode "
                + "JOIN Users u1 ON r.RequestedBy = u1.UserId "
                + "LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId "
                + "LEFT JOIN ImportList il ON r.RequestId = il.RequestId AND r.RequestTypeId = 2 "
                + "LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId "
                + "LEFT JOIN ExportList el ON r.RequestId = el.RequestId AND r.RequestTypeId = 1 "
                + "LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId";

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RequestList r = new RequestList();
                    r.setRequestId(rs.getInt("RequestId"));
                    r.setRequestDate(rs.getTimestamp("RequestDate"));
                    r.setNote(rs.getString("Note"));
                    r.setRequestTypeName(rs.getString("RequestTypeName"));
                    r.setStatusDescription(rs.getString("StatusDescription"));
                    r.setRequestedByName(rs.getString("RequestedByName"));
                    r.setApprovedByName(rs.getString("ApprovedByName"));
                    r.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                    r.setApprovalNote(rs.getString("ApprovalNote"));

                    // Gán nếu có
                    r.setImportTypeName(rs.getString("ImportTypeName")); // Có thể null
                    r.setExportTypeName(rs.getString("ExportTypeName")); // Có thể null

                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<RequestDetail> getDetailsByRequestId(int requestId) {
        List<RequestDetail> details = new ArrayList<>();
        String sql = "SELECT rd.MaterialId, rd.Quantity FROM RequestDetail rd WHERE rd.RequestId = ?";

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RequestDetail d = new RequestDetail();
                    d.setRequestId(requestId);
                    d.setMaterialId(rs.getInt("MaterialId"));
                    d.setQuantity(rs.getInt("Quantity"));
                    details.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return details;
    }

    public List<RequestList> getFilteredRequests(String type, String status, String requestedBy, String requestDate) {
        List<RequestList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.RequestId, r.RequestDate, r.Note,"
                + "   r.Status, "
                + "rt.RequestTypeName, rs.Description AS StatusDescription, "
                + "u1.FullName AS RequestedByName, "
                + "u2.FullName AS ApprovedByName, r.ApprovedDate, r.ApprovalNote, "
                + "it.ImportTypeName, et.ExportTypeName "
                + "FROM RequestList r "
                + "JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId "
                + "JOIN RequestStatus rs ON r.Status = rs.StatusCode "
                + "JOIN Users u1 ON r.RequestedBy = u1.UserId "
                + "LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId "
                + "LEFT JOIN ImportList il ON r.RequestId = il.RequestId "
                + "LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId "
                + "LEFT JOIN ExportList el ON r.RequestId = el.RequestId "
                + "LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId "
                + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName = ? ");
            params.add(type);
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.Status = ? ");;
            params.add(status);
        }

        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ? ");
            params.add("%" + requestedBy + "%");
        }

        if (requestDate != null && !requestDate.isEmpty()) {
            sql.append(" AND DATE(r.RequestDate) = ? ");
            try {
                java.sql.Date sqlDate = java.sql.Date.valueOf(requestDate);
                params.add(sqlDate);
            } catch (IllegalArgumentException e) {
                // Trường hợp chuỗi date sai định dạng yyyy-MM-dd, bỏ qua filter
            }
        }

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RequestList r = new RequestList();
                    r.setRequestId(rs.getInt("RequestId"));
                    r.setRequestDate(rs.getTimestamp("RequestDate"));
                    r.setNote(rs.getString("Note"));
                    r.setStatus(rs.getString("Status"));

                    r.setRequestTypeName(rs.getString("RequestTypeName"));
                    r.setStatusDescription(rs.getString("StatusDescription"));
                    r.setRequestedByName(rs.getString("RequestedByName"));
                    r.setApprovedByName(rs.getString("ApprovedByName"));
                    r.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                    r.setApprovalNote(rs.getString("ApprovalNote"));

                    String importType = rs.getString("ImportTypeName");
                    String exportType = rs.getString("ExportTypeName");
                    if (importType != null) {
                        r.setImportTypeName(importType);
                    }
                    if (exportType != null) {
                        r.setExportTypeName(exportType);
                    }

                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String> getAllRequestTypes() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT RequestTypeName FROM RequestType";
        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("RequestTypeName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RequestType> getAllRequestType() {
        List<RequestType> list = new ArrayList<>();
        String sql = "SELECT RequestTypeId, RequestTypeName FROM requesttype";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new RequestType(
                        rs.getInt("RequestTypeId"),
                        rs.getString("RequestTypeName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT CategoryId, CategoryName FROM categories";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("CategoryId"),
                        rs.getString("CategoryName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SubCategory> getAllSubCategories() {
        List<SubCategory> list = new ArrayList<>();
        String sql = "SELECT SubCategoryId, SubCategoryName, CategoryId FROM subcategories";
        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new SubCategory(
                        rs.getInt("SubCategoryId"),
                        rs.getString("SubCategoryName"),
                        rs.getInt("CategoryId")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SubCategory> getSubCategoriesByCategory(int categoryId) {
        List<SubCategory> list = new ArrayList<>();
        String sql = "SELECT SubCategoryId, SubCategoryName FROM subcategories WHERE CategoryId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    SubCategory sc = new SubCategory();
                    sc.setSubCategoryId(rs.getInt("SubCategoryId"));
                    sc.setSubCategoryName(rs.getString("SubCategoryName"));
                    sc.setCategoryId(categoryId);
                    list.add(sc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql
                = "SELECT\n"
                + "    m.MaterialId, m.MaterialName, m.SubCategoryId, m.StatusId,\n"
                + "    m.Image, m.Description, m.Quantity, m.MinQuantity,\n"
                + "    m.Price, m.CreatedAt, m.UpdatedAt,\n"
                + "    c.CategoryName, sc.SubCategoryName, ms.StatusName\n"
                + "FROM Materials m\n"
                + "JOIN SubCategories sc     ON m.SubCategoryId = sc.SubCategoryId\n"
                + "JOIN Categories c         ON sc.CategoryId    = c.CategoryId\n"
                + "JOIN MaterialStatus ms    ON m.StatusId       = ms.StatusId\n"
                + "WHERE m.StatusId = 1"; // only active materials

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setSubCategoryId(rs.getInt("SubCategoryId"));
                m.setStatusId(rs.getInt("StatusId"));
                m.setImage(rs.getString("Image"));
                m.setDescription(rs.getString("Description"));
                m.setQuantity(rs.getInt("Quantity"));
                m.setMinQuantity(rs.getInt("MinQuantity"));
                m.setPrice(rs.getDouble("Price"));
                m.setCreatedAt(rs.getTimestamp("CreatedAt"));
                m.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                m.setCategoryName(rs.getString("CategoryName"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                m.setStatusName(rs.getString("StatusName"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Material> getMaterialsBySubCategory(int subCategoryId) {
        return getAllMaterials().stream()
                .filter(m -> m.getSubCategoryId() == subCategoryId)
                .toList();
    }

    public boolean createRequest(int requestedBy, int requestTypeId, String note, List<RequestDetail> details) {
        String sqlReq = "INSERT INTO requestlist (RequestedBy, RequestDate, RequestTypeId, Note, Status) "
                + "VALUES (?, NOW(), ?, ?, 'Pending')";
        String sqlDetail = "INSERT INTO requestdetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            // insert request
            PreparedStatement stReq = connection.prepareStatement(sqlReq, Statement.RETURN_GENERATED_KEYS);
            stReq.setInt(1, requestedBy);
            stReq.setInt(2, requestTypeId);
            stReq.setString(3, note);
            stReq.executeUpdate();

            ResultSet rs = stReq.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Failed to retrieve RequestId.");
            }
            int requestId = rs.getInt(1);

            // insert details
            PreparedStatement stDet = connection.prepareStatement(sqlDetail);
            for (RequestDetail d : details) {
                stDet.setInt(1, requestId);
                stDet.setInt(2, d.getMaterialId());
                stDet.setInt(3, d.getQuantity());
                stDet.addBatch();
            }
            stDet.executeBatch();

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignore) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public int getMaterialStock(int materialId) throws SQLException {
        String sql = "SELECT Quantity FROM Materials WHERE MaterialId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, materialId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Quantity");
                }
            }
        }
        return 0;
    }

    public void assignImportTask(int requestId, int staffId) {
        String sql = "UPDATE ImportList SET HandledBy = ? WHERE RequestId = ?";
        DBContext db = new DBContext(); // Thêm dòng này

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffId);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignExportTask(int requestId, int staffId) {
        String sql = "UPDATE ExportList SET HandledBy = ? WHERE RequestId = ?";
        DBContext db = new DBContext(); // Thêm dòng này

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RequestList> getAssignedRequestsByStaffId(int staffId) {
        List<RequestList> list = new ArrayList<>();
        String sql = "SELECT * FROM RequestList WHERE Status = 'Approved' AND AssignedStaffId = ?";
        DBContext db = new DBContext();
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestedBy(rs.getInt("RequestedBy"));
                r.setRequestDate(rs.getTimestamp("RequestDate"));
                r.setRequestTypeId(rs.getInt("RequestTypeId"));
                r.setNote(rs.getString("Note"));
                r.setStatus(rs.getString("Status"));
                r.setApprovedBy(rs.getInt("ApprovedBy"));
                r.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                r.setAssignedStaffId(rs.getInt("AssignedStaffId"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
