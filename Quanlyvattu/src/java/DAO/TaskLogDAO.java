package DAO;

import dal.DBContext;
import model.TaskLog;
import model.TaskSlipDetail;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TaskLogDAO extends DBContext {

    public boolean insertTaskLogWithDetails(Connection conn, int requestId, int requestTypeId, int staffId,
            List<Integer> materialIds, List<Integer> quantities,
            String slipCode) throws SQLException {
        String insertLogSql = "INSERT INTO TaskLog (RequestId, RequestTypeId, StaffId, SlipCode) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertLogSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, requestId);
            ps.setInt(2, requestTypeId);
            ps.setInt(3, staffId);
            ps.setString(4, slipCode);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int taskId = rs.getInt(1);

                    String insertDetailSql = "INSERT INTO TaskSlipDetail (TaskId, MaterialId, Quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailSql)) {
                        for (int i = 0; i < materialIds.size(); i++) {
                            detailStmt.setInt(1, taskId);
                            detailStmt.setInt(2, materialIds.get(i));
                            detailStmt.setInt(3, quantities.get(i));
                            detailStmt.addBatch();
                        }
                        int[] results = detailStmt.executeBatch();
                        for (int r : results) {
                            if (r <= 0) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<TaskSlipDetail> getSlipDetailsByTaskId(Connection conn, int taskId) throws SQLException {
        List<TaskSlipDetail> details = new ArrayList<>();
        String sql = """
            SELECT tsd.MaterialId, tsd.Quantity, m.MaterialName
            FROM TaskSlipDetail tsd
            JOIN Materials m ON tsd.MaterialId = m.MaterialId
            WHERE tsd.TaskId = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TaskSlipDetail d = new TaskSlipDetail();
                d.setMaterialId(rs.getInt("MaterialId"));
                d.setQuantity(rs.getInt("Quantity"));
                d.setMaterialName(rs.getString("MaterialName"));
                details.add(d);
            }
        }

        return details;
    }

    public TaskLog getLatestTaskLogByRequestId(Connection conn, int requestId) throws SQLException {
        String sql = """
        SELECT tl.TaskId, tl.RequestId, tl.RequestTypeId, tl.StaffId, tl.CreatedAt,
               tl.SlipCode,
               u.FullName AS StaffName, rt.RequestTypeName
        FROM TaskLog tl
        JOIN Users u ON tl.StaffId = u.UserId
        JOIN RequestList rl ON tl.RequestId = rl.RequestId
        JOIN RequestSubType rst ON rl.SubTypeId = rst.SubTypeId
        JOIN RequestType rt ON rst.RequestTypeId = rt.RequestTypeId
        WHERE tl.RequestId = ?
        ORDER BY tl.CreatedAt DESC
        LIMIT 1
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaskLog log = new TaskLog();
                int taskId = rs.getInt("TaskId");
                log.setTaskId(taskId);
                log.setRequestId(rs.getInt("RequestId"));
                log.setRequestTypeId(rs.getInt("RequestTypeId"));
                log.setStaffId(rs.getInt("StaffId"));
                log.setCreatedAt(rs.getTimestamp("CreatedAt"));
                log.setSlipCode(rs.getString("SlipCode")); // ✅ Thêm dòng này
                log.setStaffName(rs.getString("StaffName"));
                log.setRequestTypeName(rs.getString("RequestTypeName"));
                log.setSlipDetails(getSlipDetailsByTaskId(conn, taskId));
                return log;
            }
        }

        return null;
    }

    public TaskLog getTaskLogByTaskId(Connection conn, int taskId) throws SQLException {
        String sql = """
        SELECT tl.TaskId, tl.RequestId, tl.RequestTypeId, tl.StaffId, tl.CreatedAt,
               tl.SlipCode,
               u.FullName AS StaffName, rt.RequestTypeName
        FROM TaskLog tl
        JOIN Users u ON tl.StaffId = u.UserId
        JOIN RequestList rl ON tl.RequestId = rl.RequestId
        JOIN RequestSubType rst ON rl.SubTypeId = rst.SubTypeId
        JOIN RequestType rt ON rst.RequestTypeId = rt.RequestTypeId
        WHERE tl.TaskId = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaskLog log = new TaskLog();
                log.setTaskId(taskId);
                log.setRequestId(rs.getInt("RequestId"));
                log.setRequestTypeId(rs.getInt("RequestTypeId"));
                log.setStaffId(rs.getInt("StaffId"));
                log.setCreatedAt(rs.getTimestamp("CreatedAt"));
                log.setSlipCode(rs.getString("SlipCode")); // ✅ Thêm dòng này
                log.setStaffName(rs.getString("StaffName"));
                log.setRequestTypeName(rs.getString("RequestTypeName"));
                log.setSlipDetails(getSlipDetailsByTaskId(conn, taskId));
                return log;
            }
        }

        return null;
    }

    public List<TaskLog> getGroupedTaskLogsByRequestId(Connection conn, int requestId) throws SQLException {
        List<TaskLog> taskLogs = new ArrayList<>();
        String sql = """
        SELECT tl.TaskId, tl.RequestId, tl.RequestTypeId, tl.StaffId, tl.CreatedAt,
               tl.SlipCode,
               u.FullName AS StaffName, rt.RequestTypeName
        FROM TaskLog tl
        JOIN Users u ON tl.StaffId = u.UserId
        JOIN RequestList rl ON tl.RequestId = rl.RequestId
        JOIN RequestSubType rst ON rl.SubTypeId = rst.SubTypeId
        JOIN RequestType rt ON rst.RequestTypeId = rt.RequestTypeId
        WHERE tl.RequestId = ?
        ORDER BY tl.CreatedAt DESC
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TaskLog log = new TaskLog();
                int taskId = rs.getInt("TaskId");
                log.setTaskId(taskId);
                log.setRequestId(rs.getInt("RequestId"));
                log.setRequestTypeId(rs.getInt("RequestTypeId"));
                log.setStaffId(rs.getInt("StaffId"));
                log.setCreatedAt(rs.getTimestamp("CreatedAt"));
                log.setStaffName(rs.getString("StaffName"));
                log.setRequestTypeName(rs.getString("RequestTypeName"));
                log.setSlipCode(rs.getString("SlipCode")); // ✅ THÊM DÒNG NÀY
                log.setSlipDetails(getSlipDetailsByTaskId(conn, taskId));
                taskLogs.add(log);
            }
        }

        return taskLogs;
    }

    public String generateSlipCode(Connection conn) throws SQLException {
        String year = String.valueOf(LocalDate.now().getYear());
        String prefix = "SLP-" + year;
        String likePattern = prefix + "-%";

        String sql = "SELECT MAX(SlipCode) FROM TaskLog WHERE SlipCode LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, likePattern);
            try (ResultSet rs = ps.executeQuery()) {
                int nextNumber = 1;
                if (rs.next()) {
                    String lastCode = rs.getString(1); // SLP-2025-000123
                    if (lastCode != null) {
                        String[] parts = lastCode.split("-");
                        nextNumber = Integer.parseInt(parts[2]) + 1;
                    }
                }
                return String.format("%s-%06d", prefix, nextNumber);
            }
        }
    }

    public List<TaskLog> getGroupedTaskLogsByRequestId(int requestId) {
        try (Connection conn = new DBContext().getConnection()) {
            return getGroupedTaskLogsByRequestId(conn, requestId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
