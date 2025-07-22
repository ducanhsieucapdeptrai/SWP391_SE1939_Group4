package DAO;

import dal.DBContext;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.RequestList;
import model.RequestDetail;
import model.RequestDetailItem;
import model.RequestType;
import model.Category;
import model.SubCategory;
import model.Material;
import dal.DBContext;

public class RequestDAO extends DBContext {

    public RequestList getRequestById(int requestId) {
        RequestList request = null;
        String sql = """
        SELECT r.RequestId, r.RequestDate, r.Note, r.Status,
               rt.RequestTypeName, rs.Description AS StatusDescription,
               u1.FullName AS RequestedByName,
               u2.FullName AS ApprovedByName, r.ApprovedDate, r.ApprovalNote,
               rst.SubTypeName
        FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        JOIN RequestStatus rs ON r.Status = rs.StatusCode
        JOIN Users u1 ON r.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId
        LEFT JOIN RequestSubType rst ON r.SubTypeId = rst.SubTypeId
        WHERE r.RequestId = ?
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
                    request.setSubTypeName(rs.getString("SubTypeName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return request;
    }

    public List<RequestList> getAllRequests() {
        List<RequestList> list = new ArrayList<>();
        String sql = """
        SELECT r.RequestId, r.RequestDate, r.Note,
               rt.RequestTypeName, rs.Description AS StatusDescription,
               u1.FullName AS RequestedByName,
               u2.FullName AS ApprovedByName, r.ApprovedDate, r.ApprovalNote,
               rst.SubTypeName
        FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        JOIN RequestStatus rs ON r.Status = rs.StatusCode
        JOIN Users u1 ON r.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId
        LEFT JOIN RequestSubType rst ON r.SubTypeId = rst.SubTypeId
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
                    r.setSubTypeName(rs.getString("SubTypeName"));
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

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
                "SELECT r.RequestId, r.RequestDate, r.Note, r.Status, "
                + "rt.RequestTypeName, rs.Description AS StatusDescription, "
                + "u1.FullName AS RequestedByName, "
                + "u2.FullName AS ApprovedByName, r.ApprovedDate, r.ApprovalNote, "
                + "rst.SubTypeName "
                + "FROM RequestList r "
                + "JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId "
                + "JOIN RequestStatus rs ON r.Status = rs.StatusCode "
                + "JOIN Users u1 ON r.RequestedBy = u1.UserId "
                + "LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId "
                + "LEFT JOIN RequestSubType rst ON r.SubTypeId = rst.SubTypeId "
                + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName = ? ");
            params.add(type);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.Status = ? ");
            params.add(status);
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ? ");
            params.add("%" + requestedBy + "%");
        }
        if (requestDate != null && !requestDate.isEmpty()) {
            try {
                java.sql.Date sqlDate = java.sql.Date.valueOf(requestDate);
                sql.append(" AND DATE(r.RequestDate) = ? ");
                params.add(sqlDate);
            } catch (IllegalArgumentException e) {
                // Ignore invalid date
            }
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
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
                    r.setSubTypeName(rs.getString("SubTypeName")); // << thay import/export
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

        Connection conn = null;
        try {
            conn = getConnection();
            if (conn == null || conn.isClosed()) {
                conn = getConnection();
            }
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("RequestTypeName"));
                }
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
        String sql = "SELECT m.MaterialId, m.MaterialName, m.SubCategoryId, m.StatusId, "
                + "m.Image, m.Description, m.Quantity, m.MinQuantity, "
                + "m.Price, m.CreatedAt, m.UpdatedAt, "
                + "c.CategoryName, sc.SubCategoryName, ms.StatusName "
                + "FROM Materials m "
                + "JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId "
                + "JOIN Categories c ON sc.CategoryId = c.CategoryId "
                + "JOIN MaterialStatus ms ON m.StatusId = ms.StatusId "
                + "WHERE m.StatusId = 1";

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

    public boolean createRequest(int requestedBy, int requestTypeId, String note, Integer projectId, List<RequestDetail> details) {
        String sqlReq = "INSERT INTO requestlist (RequestedBy, RequestDate, RequestTypeId, Note, Status, ProjectId) VALUES (?, NOW(), ?, ?, 'Pending', ?)";
        String sqlDetail = "INSERT INTO requestdetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            PreparedStatement stReq = connection.prepareStatement(sqlReq, Statement.RETURN_GENERATED_KEYS);
            stReq.setInt(1, requestedBy);
            stReq.setInt(2, requestTypeId);
            stReq.setString(3, note);
            if (projectId != null) {
                stReq.setInt(4, projectId);
            } else {
                stReq.setNull(4, java.sql.Types.INTEGER);
            }
            stReq.executeUpdate();

            ResultSet rs = stReq.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Failed to retrieve RequestId.");
            }
            int requestId = rs.getInt(1);

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

    public List<RequestList> getSimpleRequestsByUser(int userId) {
        List<RequestList> list = new ArrayList<>();
        String sql = """
        SELECT 
            r.RequestId, r.RequestDate, r.Note, r.Status, r.ApprovalNote,
            rt.RequestTypeName,
            (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) AS POCount,
            (SELECT Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) AS POStatus
        FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        WHERE r.RequestedBy = ?
        ORDER BY r.RequestDate DESC
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestDate(rs.getDate("RequestDate"));
                r.setNote(rs.getString("Note"));
                r.setStatus(rs.getString("Status"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));

                int poCount = rs.getInt("POCount");
                r.setPoCount(poCount);
                r.setHasPO(poCount > 0);
                r.setPoStatus(rs.getString("POStatus"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<RequestList> getSimpleRequestsByUserAndStatus(int userId, String status) {
        List<RequestList> list = new ArrayList<>();
        String sql = """
        SELECT 
            r.RequestId, r.RequestDate, r.Note, r.Status, r.ApprovalNote,
            rt.RequestTypeName,
            (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) AS POCount,
            (SELECT Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) AS POStatus
        FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        WHERE r.RequestedBy = ? AND r.Status = ?
        ORDER BY r.RequestDate DESC
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestDate(rs.getDate("RequestDate"));
                r.setNote(rs.getString("Note"));
                r.setStatus(rs.getString("Status"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));

                int poCount = rs.getInt("POCount");
                r.setPoCount(poCount);
                r.setHasPO(poCount > 0);
                r.setPoStatus(rs.getString("POStatus"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<RequestList> getSimpleRequestsByUserFiltered(int userId, String statusFilter, String poStatusFilter) {
        List<RequestList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT 
            r.RequestId, r.RequestDate, r.Note, r.Status, r.ApprovalNote,
            rt.RequestTypeName,
            (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) AS POCount,
            (SELECT Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) AS POStatus
        FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        WHERE r.RequestedBy = ?
    """);

        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND r.Status = ? ");
        }

        // Nếu lọc theo poStatus
        if (poStatusFilter != null && !poStatusFilter.isEmpty()) {
            if (poStatusFilter.equals("NotCreated")) {
                sql.append(" AND (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) = 0 ");
            } else {
                sql.append(" AND (SELECT Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) = ? ");
            }
        }

        sql.append(" ORDER BY r.RequestDate DESC ");

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(paramIndex++, statusFilter);
            }

            if (poStatusFilter != null && !poStatusFilter.isEmpty() && !poStatusFilter.equals("NotCreated")) {
                ps.setString(paramIndex++, poStatusFilter);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestDate(rs.getDate("RequestDate"));
                r.setNote(rs.getString("Note"));
                r.setStatus(rs.getString("Status"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));

                int poCount = rs.getInt("POCount");
                r.setPoCount(poCount);
                r.setHasPO(poCount > 0);
                r.setPoStatus(rs.getString("POStatus"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
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

    public String getRequestNoteById(int requestId) {
        String sql = "SELECT Note FROM RequestList WHERE RequestId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Note");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //phan trang
    public int countSimpleRequestsByUserFiltered(int userId, String statusFilter, String poStatusFilter) {
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) 
        FROM RequestList r 
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId 
        WHERE r.RequestedBy = ?
    """);

        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND r.Status = ? ");
        }

        if (poStatusFilter != null && !poStatusFilter.isEmpty()) {
            if (poStatusFilter.equals("NotCreated")) {
                sql.append(" AND (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) = 0 ");
            } else {
                sql.append(" AND (SELECT Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) = ? ");
            }
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, userId);

            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(index++, statusFilter);
            }

            if (poStatusFilter != null && !poStatusFilter.isEmpty() && !poStatusFilter.equals("NotCreated")) {
                ps.setString(index++, poStatusFilter);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<RequestList> getSimpleRequestsByUserFilteredPaged(int userId, String statusFilter, String poStatusFilter, int page, int pageSize) {
        List<RequestList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT 
            r.RequestId, r.RequestDate, r.Note, r.Status, r.ApprovalNote,
            rt.RequestTypeName,
            (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) AS POCount,
            (SELECT Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) AS POStatus
        FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        WHERE r.RequestedBy = ?
    """);

        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND r.Status = ? ");
        }

        if (poStatusFilter != null && !poStatusFilter.isEmpty()) {
            if (poStatusFilter.equals("NotCreated")) {
                sql.append(" AND (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) = 0 ");
            } else {
                sql.append(" AND (SELECT Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) = ? ");
            }
        }

        sql.append(" ORDER BY r.RequestDate DESC LIMIT ? OFFSET ?");

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, userId);

            if (statusFilter != null && !statusFilter.isEmpty()) {
                ps.setString(index++, statusFilter);
            }

            if (poStatusFilter != null && !poStatusFilter.isEmpty() && !poStatusFilter.equals("NotCreated")) {
                ps.setString(index++, poStatusFilter);
            }

            ps.setInt(index++, pageSize);
            ps.setInt(index, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestDate(rs.getDate("RequestDate"));
                r.setNote(rs.getString("Note"));
                r.setStatus(rs.getString("Status"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));

                int poCount = rs.getInt("POCount");
                r.setPoCount(poCount);
                r.setHasPO(poCount > 0);
                r.setPoStatus(rs.getString("POStatus"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void assignImportTask(int requestId, int staffId) {
        String sql = "UPDATE ImportList SET HandledBy = ? WHERE RequestId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignExportTask(int requestId, int staffId) {
        String sql = "UPDATE ExportList SET HandledBy = ? WHERE RequestId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RequestList> getPagedRequestsByUserFiltered(int userId, String status, String poStatus, String type, int offset, int limit) {
        List<RequestList> list = new ArrayList<>();
        String sql = """
    SELECT r.*, rt.RequestTypeName,
           (SELECT COUNT(*) FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId) AS POCount,
           (SELECT po.Status FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId LIMIT 1) AS POStatus,
           EXISTS (SELECT 1 FROM RepairOrderList ro WHERE ro.RequestId = r.RequestId) AS HasRO
    FROM RequestList r
    JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
    WHERE r.RequestedBy = ?
""";

        if (status != null && !status.isEmpty()) {
            sql += " AND r.Status = ?";
        }
        if (poStatus != null && !poStatus.isEmpty()) {
            sql += " AND EXISTS (SELECT 1 FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId AND po.Status = ?)";
        }
        if (type != null && !type.isEmpty()) {
            sql += " AND rt.RequestTypeName = ?";
        }

        sql += " ORDER BY r.RequestDate DESC LIMIT ? OFFSET ?";

        try (Connection conn = getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            int idx = 1;
            stmt.setInt(idx++, userId);
            if (status != null && !status.isEmpty()) {
                stmt.setString(idx++, status);
            }
            if (poStatus != null && !poStatus.isEmpty()) {
                stmt.setString(idx++, poStatus);
            }
            if (type != null && !type.isEmpty()) {
                stmt.setString(idx++, type);
            }
            stmt.setInt(idx++, limit);
            stmt.setInt(idx, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setStatus(rs.getString("Status"));
                r.setRequestDate(rs.getDate("RequestDate"));
                r.setNote(rs.getString("Note"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                r.setPoCount(rs.getInt("POCount"));
                r.setPoStatus(rs.getString("POStatus"));
                r.setHasPO(rs.getInt("POCount") > 0);
                r.setHasRO(rs.getBoolean("HasRO"));

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countRequestsByUserWithFilters(int userId, String status, String poStatus, String type) {
        String sql = """
        SELECT COUNT(*) FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        WHERE r.RequestedBy = ?
    """;

        if (status != null && !status.isEmpty()) {
            sql += " AND r.Status = ?";
        }
        if (poStatus != null && !poStatus.isEmpty()) {
            sql += " AND EXISTS (SELECT 1 FROM PurchaseOrderList po WHERE po.RequestId = r.RequestId AND po.Status = ?)";
        }
        if (type != null && !type.isEmpty()) {
            sql += " AND rt.RequestTypeName = ?";
        }

        try (Connection conn = getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            int idx = 1;
            stmt.setInt(idx++, userId);
            if (status != null && !status.isEmpty()) {
                stmt.setString(idx++, status);
            }
            if (poStatus != null && !poStatus.isEmpty()) {
                stmt.setString(idx++, poStatus);
            }
            if (type != null && !type.isEmpty()) {
                stmt.setString(idx++, type);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<RequestList> getAssignedRequestsByStaffId(int staffId) {
        List<RequestList> list = new ArrayList<>();
        String sql = "SELECT * FROM RequestList WHERE Status = 'Approved' AND AssignedStaffId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RequestList> getUpdatedRequests() {
        List<RequestList> list = new ArrayList<>();
        String sql = """
        SELECT r.RequestId, r.RequestDate, r.Note, r.Status,
               rt.RequestTypeName, rs.Description AS StatusDescription,
               u1.FullName AS RequestedByName,
               u2.FullName AS ApprovedByName,
               r.ApprovedDate, r.ApprovalNote,
               rst.SubTypeName
        FROM RequestList r
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        JOIN RequestStatus rs ON r.Status = rs.StatusCode
        JOIN Users u1 ON r.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId
        LEFT JOIN RequestSubType rst ON r.SubTypeId = rst.SubTypeId
        WHERE r.IsUpdated = TRUE
    """;

        try (Connection conn = connection; PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
                r.setSubTypeName(rs.getString("SubTypeName")); // <- thay thế import/export type

                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public String getRequestTypeName(int requestId) throws SQLException {
        String sql = "SELECT rt.RequestTypeName FROM RequestList rl "
                + "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId "
                + "WHERE rl.RequestId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("RequestTypeName");
                }
            }
        }
        return null;
    }

    public boolean updateActualQuantities(Connection conn, int requestId, List<RequestDetailItem> items, String requestType) throws SQLException {
        String updateDetailSql = "UPDATE RequestDetail SET ActualQuantity = ? WHERE RequestId = ? AND MaterialId = ?";
        try (PreparedStatement detailStmt = conn.prepareStatement(updateDetailSql)) {
            for (RequestDetailItem item : items) {
                int requestedQty = item.getQuantity();

                if ("Import".equalsIgnoreCase(requestType) || "Purchase".equalsIgnoreCase(requestType)) {
                    if (item.getActualQuantity() < requestedQty) {
                        return false;
                    }
                } else if ("Export".equalsIgnoreCase(requestType) || "Repair".equalsIgnoreCase(requestType)) {
                    if (item.getActualQuantity() != requestedQty) {
                        return false;
                    }
                }

                detailStmt.setInt(1, item.getActualQuantity());
                detailStmt.setInt(2, requestId);
                detailStmt.setInt(3, item.getMaterialId());
                detailStmt.addBatch();

                String stockSql;
                if ("Import".equalsIgnoreCase(requestType) || "Purchase".equalsIgnoreCase(requestType)) {
                    stockSql = "UPDATE Materials SET Quantity = Quantity + ? WHERE MaterialId = ?";
                } else {
                    stockSql = "UPDATE Materials SET Quantity = Quantity - ? WHERE MaterialId = ? AND Quantity >= ?";
                }

                try (PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
                    stockStmt.setInt(1, item.getActualQuantity());
                    stockStmt.setInt(2, item.getMaterialId());
                    if (stockSql.contains("AND Quantity >=")) {
                        stockStmt.setInt(3, item.getActualQuantity());
                    }

                    int affected = stockStmt.executeUpdate();
                    if (affected <= 0) {
                        return false;
                    }
                }
            }

            int[] result = detailStmt.executeBatch();
            for (int r : result) {
                if (r <= 0) {
                    return false;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE RequestList SET IsUpdated = TRUE WHERE RequestId = ?")) {
                stmt.setInt(1, requestId);
                stmt.executeUpdate();
            }

            return true;
        }
    }

    private int getRequestedQuantity(Connection conn, int requestId, int materialId) throws SQLException {
        String sql = "SELECT Quantity FROM RequestDetail WHERE RequestId = ? AND MaterialId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.setInt(2, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Quantity");
                }
            }
        }
        return -1;
    }

    public List<RequestDetailItem> getRequestDetails(int requestId) throws SQLException {
        List<RequestDetailItem> items = new ArrayList<>();
        String sql = "SELECT rd.RequestId, rd.MaterialId, rd.Quantity, rd.ActualQuantity, "
                + "m.MaterialName, rt.RequestTypeName, rl.Note, rl.Status, m.Quantity as StockQuantity, m.Price "
                + "FROM RequestDetail rd "
                + "JOIN Materials m ON rd.MaterialId = m.MaterialId "
                + "JOIN RequestList rl ON rd.RequestId = rl.RequestId "
                + "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId "
                + "WHERE rd.RequestId = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                RequestDetailItem item = new RequestDetailItem();
                item.setRequestId(rs.getInt("RequestId"));
                item.setMaterialId(rs.getInt("MaterialId"));
                item.setMaterialName(rs.getString("MaterialName"));
                item.setRequestTypeName(rs.getString("RequestTypeName"));
                item.setQuantity(rs.getInt("Quantity"));
                item.setActualQuantity(rs.getInt("ActualQuantity"));
                item.setNote(rs.getString("Note"));
                item.setStatus(rs.getString("Status")); // ✅ thêm dòng này
                item.setStockQuantity(rs.getInt("StockQuantity"));
                item.setPrice(rs.getDouble("Price"));
                items.add(item);
            }
        }
        return items;
    }

    public boolean markRequestUpdated(int requestId) throws SQLException {
        String sql = "UPDATE RequestList SET IsUpdated = TRUE WHERE RequestId = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean saveActualQuantities(int requestId, List<RequestDetailItem> items, Connection conn) throws SQLException {
        String sql = "UPDATE RequestDetail SET ActualQuantity = ? WHERE RequestId = ? AND MaterialId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (RequestDetailItem item : items) {
                stmt.setInt(1, item.getActualQuantity());
                stmt.setInt(2, requestId);
                stmt.setInt(3, item.getMaterialId());
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            for (int res : results) {
                if (res <= 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isExportForRepair(Connection conn, int requestId) throws SQLException {
        String sql = """
        SELECT 1
        FROM RequestList r
        JOIN RequestSubType s ON r.SubTypeId = s.SubTypeId
        WHERE r.RequestId = ?
          AND r.RequestTypeId = 1  -- Export
          AND s.SubTypeName = 'For Equipment Repair'
    """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // Detect export for construction
    private boolean isExportForConstruction(Connection conn, int requestId) throws SQLException {
        String sql = """
        SELECT 1
        FROM RequestList r
        JOIN RequestSubType s ON r.SubTypeId = s.SubTypeId
        WHERE r.RequestId = ?
          AND r.RequestTypeId = 1  -- Export
          AND s.SubTypeName = 'For Construction'
    """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // Create import request for 'Returned from Usage' (SubTypeId = 5)
    private void createImportReturnFromUsage(Connection conn, int exportRequestId, int approverId) throws SQLException {
        // Step 1: Insert into RequestList with Approved status
        String insertRequest = """
        INSERT INTO RequestList (RequestTypeId, SubTypeId, RequestedBy, RequestDate, Status, Note, ApprovedBy, ApprovedDate)
        SELECT 2, 5, RequestedBy, NOW(), 'Approved', CONCAT('Auto-created from Export Request ID ', RequestId), ?, NOW()
        FROM RequestList WHERE RequestId = ?
    """;
        int newRequestId = -1;
        try (PreparedStatement ps = conn.prepareStatement(insertRequest, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, approverId);
            ps.setInt(2, exportRequestId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                newRequestId = rs.getInt(1);
            }
        }
        // Step 2: Insert RequestDetail
        if (newRequestId > 0) {
            String insertDetails = """
            INSERT INTO RequestDetail (RequestId, MaterialId, Quantity, ActualQuantity)
            SELECT ?, MaterialId, ActualQuantity, 0
            FROM RequestDetail
            WHERE RequestId = ?
        """;
            try (PreparedStatement ps = conn.prepareStatement(insertDetails)) {
                ps.setInt(1, newRequestId);
                ps.setInt(2, exportRequestId);
                ps.executeUpdate();
            }
        }
    }

    // Public wrapper for construction export completion
    public void handleExportCompletionForConstruction(Connection conn, int exportRequestId, int approverId) throws SQLException {
        if (isExportForConstruction(conn, exportRequestId)) {
            String statusSql = "SELECT Status FROM RequestList WHERE RequestId = ?";
            try (PreparedStatement ps = conn.prepareStatement(statusSql)) {
                ps.setInt(1, exportRequestId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && "Completed".equalsIgnoreCase(rs.getString("Status"))) {
                        createImportReturnFromUsage(conn, exportRequestId, approverId);
                    }
                }
            }
        }
    }

    // Public wrapper to be called after export completion
    public void handleExportCompletionForRepair(Connection conn, int exportRequestId, int approverId) throws SQLException {
        // Only proceed if this is an export-for-repair and is completed
        if (isExportForRepair(conn, exportRequestId)) {
            // Check if the export request is now completed
            String statusSql = "SELECT Status FROM RequestList WHERE RequestId = ?";
            try (PreparedStatement ps = conn.prepareStatement(statusSql)) {
                ps.setInt(1, exportRequestId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && "Completed".equalsIgnoreCase(rs.getString("Status"))) {
                        createImportReturnFromRepair(conn, exportRequestId, approverId);
                    }
                }
            }
        }
    }

    // Original private method remains unchanged
    private void createImportReturnFromRepair(Connection conn, int exportRequestId, int approverId) throws SQLException {
        // Step 1: Insert into RequestList with Approved status
        String insertRequest = """
        INSERT INTO RequestList (RequestTypeId, SubTypeId, RequestedBy, RequestDate, Status, Note, ApprovedBy, ApprovedDate)
        SELECT 2, 4, RequestedBy, NOW(), 'Approved', CONCAT('Auto-created from Export Request ID ', RequestId), ?, NOW()
        FROM RequestList WHERE RequestId = ?
    """;

        int newRequestId = -1;

        try (PreparedStatement ps = conn.prepareStatement(insertRequest, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, approverId);
            ps.setInt(2, exportRequestId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                newRequestId = rs.getInt(1);
            }
        }

        // Step 2: Insert RequestDetail
        if (newRequestId > 0) {
            String insertDetails = """
            INSERT INTO RequestDetail (RequestId, MaterialId, Quantity, ActualQuantity)
            SELECT ?, MaterialId, ActualQuantity, 0
            FROM RequestDetail
            WHERE RequestId = ?
        """;

            try (PreparedStatement ps = conn.prepareStatement(insertDetails)) {
                ps.setInt(1, newRequestId);
                ps.setInt(2, exportRequestId);
                ps.executeUpdate();
            }
        }
    }

    public void updateStatusIfCompleted(Connection conn, int requestId) throws SQLException {
        String sql = """
        UPDATE RequestList
        SET Status = 'Completed'
        WHERE RequestId = ?
          AND NOT EXISTS (
              SELECT 1
              FROM RequestDetail
              WHERE RequestId = ?
                AND (ActualQuantity IS NULL OR ActualQuantity < Quantity)
          )
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        }
    }

    public List<RequestList> getFilteredRequestsByPage(String type, String status, String requestedBy, String requestDate, int offset, int pageSize) {
        List<RequestList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT rl.*, 
               u1.FullName AS requestedByName,
               u2.FullName AS approvedByName,
               rt.RequestTypeName,
               rs.Description AS statusDescription,
               rst.SubTypeName
        FROM RequestList rl
        LEFT JOIN Users u1 ON rl.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON rl.ApprovedBy = u2.UserId
        LEFT JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId
        LEFT JOIN RequestStatus rs ON rl.Status = rs.StatusCode
        LEFT JOIN RequestSubType rst ON rl.SubTypeId = rst.SubTypeId
        WHERE rt.RequestTypeName IN ('Export', 'Purchase', 'Repair')
    """);

        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName LIKE ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND rl.Status LIKE ?");
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ?");
        }
        if (requestDate != null && !requestDate.isEmpty()) {
            sql.append(" AND DATE(rl.RequestDate) = ?");
        }

        sql.append(" ORDER BY rl.RequestDate DESC LIMIT ? OFFSET ?");

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (type != null && !type.isEmpty()) {
                ps.setString(index++, type);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(index++, status);
            }
            if (requestedBy != null && !requestedBy.isEmpty()) {
                ps.setString(index++, "%" + requestedBy + "%");
            }
            if (requestDate != null && !requestDate.isEmpty()) {
                ps.setString(index++, requestDate);
            }
            ps.setInt(index++, pageSize);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestDate(rs.getTimestamp("RequestDate"));
                r.setRequestedByName(rs.getString("requestedByName"));
                r.setApprovedByName(rs.getString("approvedByName"));
                r.setNote(rs.getString("Note"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                r.setStatus(rs.getString("Status"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setStatusDescription(rs.getString("statusDescription"));
                r.setSubTypeName(rs.getString("SubTypeName"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countFilteredRequests(String type, String status, String requestedBy, String requestDate) {
        int count = 0;
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) 
        FROM RequestList rl
        LEFT JOIN Users u1 ON rl.RequestedBy = u1.UserId
        LEFT JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId
        WHERE rt.RequestTypeName IN ('Export', 'Purchase', 'Repair')
    """);

        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName LIKE ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND rl.Status LIKE ?");
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ?");
        }
        if (requestDate != null && !requestDate.isEmpty()) {
            sql.append(" AND DATE(rl.RequestDate) = ?");
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (type != null && !type.isEmpty()) {
                ps.setString(index++, type);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(index++, status);
            }
            if (requestedBy != null && !requestedBy.isEmpty()) {
                ps.setString(index++, "%" + requestedBy + "%");
            }
            if (requestDate != null && !requestDate.isEmpty()) {
                ps.setString(index++, requestDate);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<RequestList> getCompletedRequests() throws SQLException {
        List<RequestList> list = new ArrayList<>();
        String sql = """
            SELECT r.RequestId, r.RequestDate, r.RequestTypeId, t.RequestTypeName,
                   r.Status, s.Description AS StatusDescription, r.Note
            FROM RequestList r
            JOIN RequestType t ON r.RequestTypeId = t.RequestTypeId
            JOIN RequestStatus s ON r.Status = s.StatusCode
            WHERE r.Status = 'Completed'
            ORDER BY r.RequestDate DESC
        """;

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RequestList req = new RequestList();
                req.setRequestId(rs.getInt("RequestId"));
                req.setRequestDate(rs.getTimestamp("RequestDate"));
                req.setRequestTypeId(rs.getInt("RequestTypeId"));
                req.setRequestTypeName(rs.getString("RequestTypeName"));
                req.setStatus(rs.getString("Status"));
                req.setStatusDescription(rs.getString("StatusDescription"));
                req.setNote(rs.getString("Note"));
                list.add(req);
            }
        }
        return list;
    }

    public List<RequestList> getCompletedRequestsFiltered(
            String type,
            String requestedBy,
            Date createdDate,
            Date finishDate,
            String sortColumn,
            String sortDirection,
            int offset,
            int pageSize
    ) {
        List<RequestList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT r.RequestId, r.RequestDate, r.Note, u.FullName AS RequestedByName,
               r.RequestTypeId, rt.RequestTypeName, r.Status, rs.Description AS StatusDescription,
               MAX(tl.CreatedAt) AS FinishedDate
        FROM RequestList r
        JOIN Users u ON r.RequestedBy = u.UserId
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        JOIN RequestStatus rs ON r.Status = rs.StatusCode
        LEFT JOIN TaskLog tl ON r.RequestId = tl.RequestId
        WHERE r.Status = 'Completed'
    """);

        List<Object> params = new ArrayList<>();

        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName = ?");
            params.add(type);
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u.FullName LIKE ?");
            params.add("%" + requestedBy + "%");
        }
        if (createdDate != null) {
            sql.append(" AND DATE(r.RequestDate) = ?");
            params.add(createdDate);
        }
        if (finishDate != null) {
            sql.append(" AND EXISTS (");
            sql.append(" SELECT 1 FROM TaskLog t");
            sql.append(" WHERE t.RequestId = r.RequestId AND DATE(t.CreatedAt) = ?");
            sql.append(" )");
            params.add(finishDate);
        }

        sql.append("""
        GROUP BY r.RequestId, r.RequestDate, r.Note, u.FullName,
                 r.RequestTypeId, rt.RequestTypeName, r.Status, rs.Description
    """);

        // ✅ Validate sort column
        String safeSortColumn;
        switch (sortColumn) {
            case "requestId" ->
                safeSortColumn = "r.RequestId";
            case "requestDate" ->
                safeSortColumn = "r.RequestDate";
            case "finishedDate" ->
                safeSortColumn = "FinishedDate";
            default ->
                safeSortColumn = "FinishedDate";
        }

        String safeDirection = "asc".equalsIgnoreCase(sortDirection) ? "ASC" : "DESC";
        sql.append(" ORDER BY ").append(safeSortColumn).append(" ").append(safeDirection);

        sql.append(" LIMIT ?, ?");

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Object param : params) {
                ps.setObject(idx++, param);
            }
            ps.setInt(idx++, offset);
            ps.setInt(idx, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RequestList req = new RequestList();
                    req.setRequestId(rs.getInt("RequestId"));
                    req.setRequestDate(rs.getTimestamp("RequestDate"));
                    req.setNote(rs.getString("Note"));
                    req.setRequestedByName(rs.getString("RequestedByName"));
                    req.setRequestTypeId(rs.getInt("RequestTypeId"));
                    req.setRequestTypeName(rs.getString("RequestTypeName"));
                    req.setFinishedDate(rs.getTimestamp("FinishedDate"));
                    list.add(req);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countCompletedRequestsFiltered(String type, String requestedBy, Date createdDate, Date finishDate) {
        int count = 0;
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(DISTINCT r.RequestId)
        FROM RequestList r
        JOIN Users u ON r.RequestedBy = u.UserId
        JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId
        LEFT JOIN TaskLog tl ON r.RequestId = tl.RequestId
        WHERE r.Status = 'Completed'
    """);

        List<Object> params = new ArrayList<>();

        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName = ?");
            params.add(type);
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u.FullName LIKE ?");
            params.add("%" + requestedBy + "%");
        }
        if (createdDate != null) {
            sql.append(" AND DATE(r.RequestDate) = ?");
            params.add(createdDate);
        }
        if (finishDate != null) {
            sql.append(" AND EXISTS (");
            sql.append(" SELECT 1 FROM TaskLog t");
            sql.append(" WHERE t.RequestId = r.RequestId AND DATE(t.CreatedAt) = ?");
            sql.append(" )");
            params.add(finishDate);
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<RequestList> getRequestsByProject(int projectId) {
        List<RequestList> list = new ArrayList<>();
        String sql = """
    SELECT r.RequestId, r.RequestTypeId, t.RequestTypeName AS RequestTypeName,
           r.RequestedBy, u.FullName AS RequestedByName,
           r.Status, r.RequestDate
    FROM RequestList r
    JOIN RequestType t ON r.RequestTypeId = t.RequestTypeId
    JOIN Users u ON r.RequestedBy = u.UserId
    WHERE r.ProjectId = ?
    ORDER BY r.RequestId DESC
""";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RequestList req = new RequestList();
                    req.setRequestId(rs.getInt("RequestId"));
                    req.setRequestTypeId(rs.getInt("RequestTypeId"));
                    req.setRequestTypeName(rs.getString("RequestTypeName"));
                    req.setRequestedBy(rs.getInt("RequestedBy"));
                    req.setRequestedByName(rs.getString("RequestedByName"));
                    req.setStatus(rs.getString("Status"));
                    req.setRequestDate(rs.getDate("RequestDate")); // ✅ Created Date
                    list.add(req);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int createRepairRequest(int requestedBy, String note, List<RequestDetail> details) {
        int newId = -1;
        try {
            connection.setAutoCommit(false);

            // Với Repair, không có SubType → để NULL
            String insertRequestSql = "INSERT INTO RequestList (RequestTypeId, RequestedBy, RequestDate, Note, IsApproved, SubTypeId) "
                    + "VALUES (3, ?, GETDATE(), ?, 0, NULL)";
            PreparedStatement reqStmt = connection.prepareStatement(insertRequestSql, Statement.RETURN_GENERATED_KEYS);
            reqStmt.setInt(1, requestedBy);
            reqStmt.setString(2, note);
            reqStmt.executeUpdate();

            ResultSet rs = reqStmt.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
            }

            String insertDetailSql = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity, Status) VALUES (?, ?, ?, '')";
            PreparedStatement detailStmt = connection.prepareStatement(insertDetailSql);
            for (RequestDetail d : details) {
                detailStmt.setInt(1, newId);
                detailStmt.setInt(2, d.getMaterialId());
                detailStmt.setInt(3, d.getQuantity());
                detailStmt.addBatch();
            }
            detailStmt.executeBatch();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            newId = -1;
        }
        return newId;
    }

    public void updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE RequestList SET Status = ? WHERE RequestId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRequestStatusToROCreated(int requestId) {
        String sql = "UPDATE RequestList SET Status = ? WHERE RequestId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "RO Created"); // hoặc Status code tương ứng nếu dùng enum hoặc mã số
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // GIỮ HÀM CŨ - Tìm theo requestTypeId

    public String getRequestTypeNameByTypeId(int typeId) throws SQLException {
        String sql = "SELECT RequestTypeName FROM RequestType WHERE RequestTypeId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("RequestTypeName");
            }
        }
        return null;
    }

    public List<Project> getAllProjects() {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT ProjectId, ProjectName FROM Project"; // sửa tên bảng nếu khác

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Project p = new Project();
                p.setProjectId(rs.getInt("ProjectId"));
                p.setProjectName(rs.getString("ProjectName"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
