package DAO;

import dal.DBContext;
import java.sql.*;
import java.util.*;
import model.RequestList;

public class AssignTaskDAO extends DBContext {

    public List<RequestList> getApprovedRequests(String type, String requestedBy, String requestDate) {
        List<RequestList> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.RequestId, r.RequestDate, r.Note, r.Status, "
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
                + "WHERE r.Status = 'Approved' AND r.AssignedStaffId IS NULL "
        );

        List<Object> params = new ArrayList<>();

        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName = ? ");
            params.add(type);
        }

        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ? ");
            params.add("%" + requestedBy + "%");
        }

        if (requestDate != null && !requestDate.isEmpty()) {
            try {
                sql.append(" AND DATE(r.RequestDate) = ? ");
                params.add(java.sql.Date.valueOf(requestDate));
            } catch (IllegalArgumentException e) {
                // Bỏ qua nếu sai format
            }
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
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
                + "WHERE r.Status = 'Approved' AND r.AssignedStaffId IS NULL "
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

    public boolean assignStaffToRequest(int requestId, int staffId) {
        String sql = "UPDATE Requests SET AssignedStaffId = ?, AssignedDate = GETDATE() WHERE RequestId = ?";
        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<RequestList> getAssignedRequestsWithFilters(int staffId, String requestType, String requestDate) {
        List<RequestList> list = new ArrayList<>();
        String sql = "SELECT rl.*, rt.RequestTypeName, u.FullName AS RequestedByName "
                + "FROM RequestList rl "
                + "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId "
                + "JOIN Users u ON rl.RequestedBy = u.UserId "
                + "WHERE rl.AssignedStaffId = ? AND rl.Status = 'Approved' ";

        if (requestType != null && !requestType.isEmpty()) {
            sql += "AND rt.RequestTypeName = ? ";
        }
        if (requestDate != null && !requestDate.isEmpty()) {
            sql += "AND DATE(rl.RequestDate) = ? ";
        }

        DBContext db = new DBContext();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            int index = 2;
            if (requestType != null && !requestType.isEmpty()) {
                ps.setString(index++, requestType);
            }
            if (requestDate != null && !requestDate.isEmpty()) {
                ps.setString(index++, requestDate);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = new RequestList();
                r.setRequestId(rs.getInt("RequestId"));
                r.setRequestedByName(rs.getString("RequestedByName"));
                r.setRequestDate(rs.getDate("RequestDate"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setNote(rs.getString("Note"));
                r.setApprovalNote(rs.getString("ApprovalNote"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllRequestTypeNames() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT RequestTypeName FROM RequestType";
        DBContext db = new DBContext();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                types.add(rs.getString("RequestTypeName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
    }
// Trong ApprovedRequestDAO hoặc RequestDAO

    public RequestList getRequestById(int requestId) {
        RequestList request = null;
        String sql = "SELECT * FROM RequestList WHERE RequestId = ?";

        DBContext db = new DBContext();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                request = new RequestList();
                request.setRequestId(rs.getInt("RequestId"));
                request.setRequestDate(rs.getDate("RequestDate"));
                request.setRequestedBy(rs.getInt("RequesterId"));
                request.setStatus(rs.getString("Status"));
                request.setAssignedStaffId(rs.getInt("AssignedStaffId"));
                // bổ sung các trường khác nếu có
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    public boolean assignRequestToStaff(int requestId, int staffId) {
        String sql = "UPDATE RequestList SET AssignedStaffId = ? WHERE RequestId = ? AND AssignedStaffId IS NULL";
        DBContext db = new DBContext();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ps.setInt(2, requestId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<RequestList> getRequestsByAssignedStaff(int staffId) {
        List<RequestList> list = new ArrayList<>();
        String sql = "SELECT * FROM RequestList WHERE AssignedStaffId = ?";
        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = new RequestList();
                // set fields...
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RequestList> getAllUnassignedApprovedRequests() {
        List<RequestList> list = new ArrayList<>();

        String sql = "SELECT * FROM RequestList WHERE Status = 'Approved' AND AssignedStaffId IS NULL";
        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestList r = new RequestList();
                // set các thuộc tính r ở đây
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy danh sách Ongoing Tasks - hàng sẽ đến trong hôm nay
    public List<RequestList> getOngoingTasksToday() {
        List<RequestList> list = new ArrayList<>();
        String sql = """
        SELECT rl.*, 
               u1.FullName AS RequestedByName,
               u2.FullName AS ApprovedByName,
               rt.RequestTypeName,
               it.ImportTypeName,
               et.ExportTypeName
        FROM RequestList rl
        JOIN Users u1 ON rl.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON rl.ApprovedBy = u2.UserId
        JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId
        LEFT JOIN ImportList il ON rl.RequestId = il.RequestId
        LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId
        LEFT JOIN ExportList el ON rl.RequestId = el.RequestId
        LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId
        WHERE rl.Status = 'Approved'
          AND rl.AssignedStaffId IS NOT NULL
          AND DATE(rl.ArrivalDate) = CURDATE()
    """;

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RequestList r = mapResultSetToRequestList(rs);
                r.setRequestedByName(rs.getString("RequestedByName"));
                r.setApprovedByName(rs.getString("ApprovedByName"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setImportTypeName(rs.getString("ImportTypeName")); // có thể null
                r.setExportTypeName(rs.getString("ExportTypeName")); // có thể null
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<RequestList> getUpcomingTasks() {
        List<RequestList> list = new ArrayList<>();
        String sql = """
        SELECT rl.*, 
               u1.FullName AS RequestedByName,
               u2.FullName AS ApprovedByName,
               rt.RequestTypeName,
               it.ImportTypeName,
               et.ExportTypeName
        FROM RequestList rl
        JOIN Users u1 ON rl.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON rl.ApprovedBy = u2.UserId
        JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId
        LEFT JOIN ImportList il ON rl.RequestId = il.RequestId
        LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId
        LEFT JOIN ExportList el ON rl.RequestId = el.RequestId
        LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId
        WHERE rl.Status = 'Approved'
          AND rl.AssignedStaffId IS NOT NULL
          AND (rl.ArrivalDate IS NULL OR DATE(rl.ArrivalDate) > CURDATE())
    """;

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RequestList r = mapResultSetToRequestList(rs);
                r.setRequestedByName(rs.getString("RequestedByName"));
                r.setApprovedByName(rs.getString("ApprovedByName"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setImportTypeName(rs.getString("ImportTypeName"));
                r.setExportTypeName(rs.getString("ExportTypeName"));
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Hàm dựng dữ liệu từ ResultSet
    private RequestList mapResultSetToRequestList(ResultSet rs) throws SQLException {
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
        r.setIsUpdated(rs.getBoolean("IsUpdated"));
        try {
            r.setArrivalDate(rs.getTimestamp("ArrivalDate"));
        } catch (Exception ignored) {
        }
        return r;
    }

    public List<RequestList> getOngoingTasksTodayFiltered(String type, String requestedBy, String requestDate) {
        List<RequestList> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT rl.*, 
               u1.FullName AS RequestedByName,
               u2.FullName AS ApprovedByName,
               rt.RequestTypeName,
               it.ImportTypeName,
               et.ExportTypeName
        FROM RequestList rl
        JOIN Users u1 ON rl.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON rl.ApprovedBy = u2.UserId
        JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId
        LEFT JOIN ImportList il ON rl.RequestId = il.RequestId
        LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId
        LEFT JOIN ExportList el ON rl.RequestId = el.RequestId
        LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId

        WHERE Status = 'Approved'
          AND IsTransferredToday = TRUE
          AND IsCompleted = FALSE;
    """);

        List<Object> params = new ArrayList<>();
        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName = ?");
            params.add(type);
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ?");
            params.add("%" + requestedBy + "%");
        }
        if (requestDate != null && !requestDate.isEmpty()) {
            sql.append(" AND DATE(rl.RequestDate) = ?");
            params.add(requestDate);
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = mapResultSetToRequestList(rs);
                r.setRequestedByName(rs.getString("RequestedByName"));
                r.setApprovedByName(rs.getString("ApprovedByName"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setImportTypeName(rs.getString("ImportTypeName"));
                r.setExportTypeName(rs.getString("ExportTypeName"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<RequestList> getUpcomingTasksFiltered(String type, String requestedBy, String requestDate) {
        List<RequestList> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT rl.*, 
               u1.FullName AS RequestedByName,
               u2.FullName AS ApprovedByName,
               rt.RequestTypeName,
               it.ImportTypeName,
               et.ExportTypeName
        FROM RequestList rl
        JOIN Users u1 ON rl.RequestedBy = u1.UserId
        LEFT JOIN Users u2 ON rl.ApprovedBy = u2.UserId
        JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId
        LEFT JOIN ImportList il ON rl.RequestId = il.RequestId
        LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId
        LEFT JOIN ExportList el ON rl.RequestId = el.RequestId
        LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId
  
        WHERE Status = 'Approved'
          AND IsTransferredToday = FALSE
          AND IsCompleted = FALSE;
   
    """);

        List<Object> params = new ArrayList<>();
        if (type != null && !type.isEmpty()) {
            sql.append(" AND rt.RequestTypeName = ?");
            params.add(type);
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            sql.append(" AND u1.FullName LIKE ?");
            params.add("%" + requestedBy + "%");
        }
        if (requestDate != null && !requestDate.isEmpty()) {
            sql.append(" AND DATE(rl.RequestDate) = ?");
            params.add(requestDate);
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RequestList r = mapResultSetToRequestList(rs);
                r.setRequestedByName(rs.getString("RequestedByName"));
                r.setApprovedByName(rs.getString("ApprovedByName"));
                r.setRequestTypeName(rs.getString("RequestTypeName"));
                r.setImportTypeName(rs.getString("ImportTypeName"));
                r.setExportTypeName(rs.getString("ExportTypeName"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String> getAllRequesterNames() {
        List<String> names = new ArrayList<>();
        String sql = """
        SELECT DISTINCT u.FullName
        FROM Users u
        JOIN RequestList r ON u.UserId = r.RequestedBy
        ORDER BY u.FullName
    """;
        DBContext db = new DBContext();
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                names.add(rs.getString("FullName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public void updateIsTransferredToday(int requestId, boolean isTransferred) {
        String sql = "UPDATE RequestList SET IsTransferredToday = ? WHERE RequestId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isTransferred);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
