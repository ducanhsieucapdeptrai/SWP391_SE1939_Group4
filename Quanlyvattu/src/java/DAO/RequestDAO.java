/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Admin
 */


import model.RequestList;
import model.RequestDetail;
import model.RequestType;
import model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dal.DBContext;

public class RequestDAO extends DBContext {
    
    // Lấy danh sách loại yêu cầu
    public List<RequestType> getAllRequestTypes() {
        List<RequestType> list = new ArrayList<>();
        String sql = "SELECT * FROM requesttype";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RequestType rt = new RequestType();
                rt.setRequestTypeId(rs.getInt("RequestTypeId"));
                rt.setRequestTypeName(rs.getString("RequestTypeName"));
                list.add(rt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy danh sách vật tư có sẵn
    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT * FROM materials WHERE StatusId = 1"; // Chỉ lấy vật tư đang hoạt động
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setQuantity(rs.getInt("Quantity"));
                m.setPrice(rs.getDouble("Price"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Tạo yêu cầu mới
    public boolean createRequest(int requestedBy, int requestTypeId, String note, List<RequestDetail> details) {
        String sqlRequest = "INSERT INTO requestlist (RequestedBy, RequestDate, RequestTypeId, Note, Status) VALUES (?, NOW(), ?, ?, 'Pending')";
        String sqlDetail = "INSERT INTO requestdetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";
        
        try {
            connection.setAutoCommit(false);
            
            // Insert request
            PreparedStatement stRequest = connection.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS);
            stRequest.setInt(1, requestedBy);
            stRequest.setInt(2, requestTypeId);
            stRequest.setString(3, note);
            stRequest.executeUpdate();
            
            // Lấy ID của request vừa tạo
            ResultSet rs = stRequest.getGeneratedKeys();
            int requestId = 0;
            if (rs.next()) {
                requestId = rs.getInt(1);
            }
            
            // Insert request details
            PreparedStatement stDetail = connection.prepareStatement(sqlDetail);
            for (RequestDetail detail : details) {
                stDetail.setInt(1, requestId);
                stDetail.setInt(2, detail.getMaterialId());
                stDetail.setInt(3, detail.getQuantity());
                stDetail.addBatch();
            }
            stDetail.executeBatch();
            
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    // Lấy thông tin chi tiết một yêu cầu (cho warehouse report)
    public RequestList getRequestById(int requestId) {
        String sql = "SELECT r.*, rt.RequestTypeName, u.FullName as RequestedByName " +
                    "FROM requestlist r " +
                    "JOIN requesttype rt ON r.RequestTypeId = rt.RequestTypeId " +
                    "JOIN users u ON r.RequestedBy = u.UserId " +
                    "WHERE r.RequestId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, requestId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                RequestList request = new RequestList();
                request.setRequestId(rs.getInt("RequestId"));
                request.setRequestedBy(rs.getInt("RequestedBy"));
                request.setRequestDate(rs.getDate("RequestDate"));
                request.setRequestTypeId(rs.getInt("RequestTypeId"));
                request.setNote(rs.getString("Note"));
                request.setStatus(rs.getString("Status"));
                request.setRequestTypeName(rs.getString("RequestTypeName"));
                request.setRequestedByName(rs.getString("RequestedByName"));
                
                // Lấy chi tiết yêu cầu
                request.setRequestDetails(getRequestDetails(requestId));
                
                return request;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy chi tiết yêu cầu
    public List<RequestDetail> getRequestDetails(int requestId) {
        List<RequestDetail> list = new ArrayList<>();
        String sql = "SELECT rd.*, m.MaterialName, m.Price " +
                    "FROM requestdetail rd " +
                    "JOIN materials m ON rd.MaterialId = m.MaterialId " +
                    "WHERE rd.RequestId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, requestId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RequestDetail detail = new RequestDetail();
                detail.setRequestId(rs.getInt("RequestId"));
                detail.setMaterialId(rs.getInt("MaterialId"));
                detail.setQuantity(rs.getInt("Quantity"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy danh sách yêu cầu đã được duyệt (cho warehouse staff)
    public List<RequestList> getApprovedRequests() {
        List<RequestList> list = new ArrayList<>();
        String sql = "SELECT r.*, rt.RequestTypeName, u.FullName as RequestedByName " +
                    "FROM requestlist r " +
                    "JOIN requesttype rt ON r.RequestTypeId = rt.RequestTypeId " +
                    "JOIN users u ON r.RequestedBy = u.UserId " +
                    "WHERE r.Status = 'Approved' " +
                    "ORDER BY r.RequestDate DESC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RequestList request = new RequestList();
                request.setRequestId(rs.getInt("RequestId"));
                request.setRequestedBy(rs.getInt("RequestedBy"));
                request.setRequestDate(rs.getDate("RequestDate"));
                request.setRequestTypeId(rs.getInt("RequestTypeId"));
                request.setNote(rs.getString("Note"));
                request.setStatus(rs.getString("Status"));
                request.setRequestTypeName(rs.getString("RequestTypeName"));
                request.setRequestedByName(rs.getString("RequestedByName"));
                list.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Cập nhật trạng thái yêu cầu sau khi xử lý kho
    public boolean updateRequestStatus(int requestId, String status, String reportNote) {
        String sql = "UPDATE requestlist SET Status = ?, ApprovalNote = ?, ApprovedDate = NOW() WHERE RequestId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            st.setString(2, reportNote);
            st.setInt(3, requestId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
