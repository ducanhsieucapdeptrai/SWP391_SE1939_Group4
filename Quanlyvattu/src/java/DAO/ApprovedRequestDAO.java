package DAO;

import dal.DBContext;
import java.sql.*;
import java.util.*;
import model.RequestList;

public class ApprovedRequestDAO extends DBContext {

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
                + "WHERE r.Status = 'Approved' "
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
                // Format sai bỏ qua
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
}
