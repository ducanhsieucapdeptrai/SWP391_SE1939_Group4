package DAO;

import dal.DBContext;
import model.TaskLog;
import model.TaskSlipDetail;

import java.sql.*;
import java.util.*;

public class TaskLogDAO extends DBContext {

    // ✅ Ghi 1 dòng log chính + chi tiết vào TaskSlipDetail
    public boolean insertTaskLogWithDetails(Connection conn, int requestId, int requestTypeId, int staffId,
                                            List<Integer> materialIds, List<Integer> quantities) throws SQLException {
        String insertLogSql = "INSERT INTO TaskLog (RequestId, RequestTypeId, StaffId) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertLogSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, requestId);
            ps.setInt(2, requestTypeId);
            ps.setInt(3, staffId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int taskId = rs.getInt(1);

                    // Ghi chi tiết vật liệu vào bảng TaskSlipDetail
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
                            if (r <= 0) return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ✅ Đọc danh sách slip đã tạo (grouped log), kèm theo vật liệu chi tiết
    public List<TaskLog> getGroupedTaskLogsByRequestId(Connection conn, int requestId) throws SQLException {
        List<TaskLog> taskLogs = new ArrayList<>();

        // 1. Lấy danh sách log (slip)
        String mainLogSql = """
            SELECT tl.TaskId, tl.RequestId, tl.RequestTypeId, tl.StaffId, tl.CreatedAt,
                   u.FullName AS StaffName, rt.RequestTypeName
            FROM TaskLog tl
            JOIN Users u ON tl.StaffId = u.UserId
            JOIN RequestType rt ON tl.RequestTypeId = rt.RequestTypeId
            WHERE tl.RequestId = ?
            ORDER BY tl.CreatedAt DESC
        """;

        try (PreparedStatement ps = conn.prepareStatement(mainLogSql)) {
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

                // 2. Lấy danh sách vật liệu chi tiết của từng log
                List<TaskSlipDetail> details = getSlipDetailsByTaskId(conn, taskId);
                log.setSlipDetails(details);

                taskLogs.add(log);
            }
        }

        return taskLogs;
    }

    // ✅ Truy xuất danh sách vật liệu chi tiết của một slip (log)
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
}
