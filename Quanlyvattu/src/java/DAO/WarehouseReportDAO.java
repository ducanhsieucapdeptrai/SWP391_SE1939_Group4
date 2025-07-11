package DAO;

import dal.DBContext;
import model.RequestList;
import model.RequestDetailItem;
import model.TaskLog;
import model.TaskSlipDetail;
import model.RequestType;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseReportDAO extends DBContext {

    // Get request info by ID
    public RequestList getRequestById(int requestId) {
        String sql = "SELECT rl.RequestId, rl.RequestedBy, u.FullName AS RequestedByName, rl.RequestDate, "
                + "rl.RequestTypeId, rt.RequestTypeName, rl.Note, rl.Status, rs.Description AS StatusDescription, "
                + "rst.SubTypeName "
                + "FROM RequestList rl JOIN Users u ON rl.RequestedBy = u.UserId "
                + "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId "
                + "LEFT JOIN RequestSubType rst ON rl.SubTypeId = rst.SubTypeId "
                + "LEFT JOIN RequestStatus rs ON rl.Status = rs.StatusCode "
                + "WHERE rl.RequestId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                RequestList request = new RequestList();
                request.setRequestId(rs.getInt("RequestId"));
                request.setRequestedBy(rs.getInt("RequestedBy"));
                request.setRequestedByName(rs.getString("RequestedByName"));
                request.setRequestDate(rs.getTimestamp("RequestDate"));
                request.setRequestType(new RequestType(rs.getInt("RequestTypeId"), rs.getString("RequestTypeName")));
                request.setNote(rs.getString("Note"));
                request.setStatus(rs.getString("Status"));
                request.setStatusDescription(rs.getString("StatusDescription"));
                request.setSubTypeName(rs.getString("SubTypeName"));
                return request;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get request details with material info and image
    public List<RequestDetailItem> getRequestDetailsById(int requestId) {
        List<RequestDetailItem> details = new ArrayList<>();
        String sql = "SELECT rd.RequestId, rd.MaterialId, m.MaterialName, rd.Quantity, rd.ActualQuantity, "
                + "m.Quantity AS StockQuantity, m.Image AS ImageUrl "
                + "FROM RequestDetail rd "
                + "JOIN Materials m ON rd.MaterialId = m.MaterialId "
                + "WHERE rd.RequestId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RequestDetailItem detail = new RequestDetailItem();
                    detail.setRequestId(rs.getInt("RequestId"));
                    detail.setMaterialId(rs.getInt("MaterialId"));
                    detail.setMaterialName(rs.getString("MaterialName"));
                    detail.setQuantity(rs.getInt("Quantity"));
                    detail.setActualQuantity(rs.getInt("ActualQuantity"));
                    detail.setStockQuantity(rs.getInt("StockQuantity"));
                    detail.setImageUrl(rs.getString("ImageUrl"));
                    details.add(detail);
                }
            }
            System.out.println("DEBUG: requestDetails.size() = " + details.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    // Get task logs grouped by date
    public Map<Date, List<TaskLog>> getTaskLogsByDate(int requestId) {
        Map<Date, List<TaskLog>> taskLogsByDate = new HashMap<>();
        String sql = "SELECT tl.TaskId, tl.RequestId, tl.RequestTypeId, rt.RequestTypeName, "
                + "tl.StaffId, u.FullName AS StaffName, tl.CreatedAt "
                + "FROM TaskLog tl JOIN Users u ON tl.StaffId = u.UserId "
                + "JOIN RequestType rt ON tl.RequestTypeId = rt.RequestTypeId "
                + "WHERE tl.RequestId = ? ORDER BY tl.CreatedAt";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TaskLog task = new TaskLog();
                task.setTaskId(rs.getInt("TaskId"));
                task.setRequestId(rs.getInt("RequestId"));
                task.setRequestTypeId(rs.getInt("RequestTypeId"));
                task.setRequestTypeName(rs.getString("RequestTypeName"));
                task.setStaffId(rs.getInt("StaffId"));
                task.setStaffName(rs.getString("StaffName"));
                task.setCreatedAt(rs.getTimestamp("CreatedAt"));
                task.setSlipDetails(getSlipDetails(task.getTaskId()));

                // Group by date (truncate time)
                Date date = new Date(rs.getTimestamp("CreatedAt").getTime());
                taskLogsByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskLogsByDate;
    }

    // Get slip details for a task
    private List<TaskSlipDetail> getSlipDetails(int taskId) {
        List<TaskSlipDetail> slipDetails = new ArrayList<>();
        String sql = "SELECT tsd.MaterialId, m.MaterialName, tsd.Quantity "
                + "FROM TaskSlipDetail tsd JOIN Materials m ON tsd.MaterialId = m.MaterialId "
                + "WHERE tsd.TaskId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TaskSlipDetail slip = new TaskSlipDetail();
                slip.setMaterialId(rs.getInt("MaterialId"));
                slip.setMaterialName(rs.getString("MaterialName"));
                slip.setQuantity(rs.getInt("Quantity"));
                slipDetails.add(slip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slipDetails;
    }
}
