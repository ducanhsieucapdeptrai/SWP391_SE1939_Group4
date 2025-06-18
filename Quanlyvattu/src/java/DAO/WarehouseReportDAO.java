package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class WarehouseReportDAO {

    // Lấy thông tin request theo ID
    public RequestList getRequestById(int requestId) {
        String sql = "SELECT r.*, u1.FullName as RequestedByName, u2.FullName as ApprovedByName, rt.RequestTypeName " +
                    "FROM RequestList r " +
                    "LEFT JOIN Users u1 ON r.RequestedBy = u1.UserId " +
                    "LEFT JOIN Users u2 ON r.ApprovedBy = u2.UserId " +
                    "LEFT JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId " +
                    "WHERE r.RequestId = ?";
        
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                RequestList request = new RequestList();
                request.setRequestId(rs.getInt("RequestId"));
                request.setRequestedBy(rs.getInt("RequestedBy"));
                request.setRequestDate(rs.getDate("RequestDate"));
                request.setRequestTypeId(rs.getInt("RequestTypeId"));
                request.setNote(rs.getString("Note"));
                request.setStatus(rs.getString("Status"));
                request.setApprovedBy(rs.getInt("ApprovedBy"));
                request.setApprovedDate(rs.getDate("ApprovedDate"));
                request.setApprovalNote(rs.getString("ApprovalNote"));
                request.setRequestedByName(rs.getString("RequestedByName"));
                request.setApprovedByName(rs.getString("ApprovedByName"));
                request.setRequestTypeName(rs.getString("RequestTypeName"));
                
                rs.close();
                ps.close();
                conn.close();
                return request;
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in getRequestById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy chi tiết request theo ID - theo mẫu RequestDetailDAO
    public List<RequestDetail> getRequestDetailsByRequestId(int requestId) {
        List<RequestDetail> list = new ArrayList<>();
        String sql = "SELECT rd.RequestId, rd.MaterialId, rd.Quantity, " +
                    "m.MaterialName, m.Price, m.Image, m.Description, " +
                    "sc.SubCategoryName, c.CategoryName " +
                    "FROM RequestDetail rd " +
                    "JOIN Materials m ON rd.MaterialId = m.MaterialId " +
                    "JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId " +
                    "JOIN Categories c ON sc.CategoryId = c.CategoryId " +
                    "WHERE rd.RequestId = ?";
        
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
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
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in getRequestDetailsByRequestId: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy danh sách import liên quan đến request
    public List<ImportList> getRelatedImportsByRequestId(int requestId) {
        List<ImportList> list = new ArrayList<>();
        String sql = "SELECT DISTINCT il.*, u.FullName as ImportedByName, it.ImportTypeName " +
                    "FROM ImportList il " +
                    "LEFT JOIN Users u ON il.ImportedBy = u.UserId " +
                    "LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId " +
                    "INNER JOIN ImportDetail id ON il.ImportId = id.ImportId " +
                    "WHERE id.MaterialId IN (" +
                    "    SELECT rd.MaterialId FROM RequestDetail rd WHERE rd.RequestId = ?" +
                    ") ORDER BY il.ImportDate DESC";
        
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ImportList importList = new ImportList();
                importList.setImportId(rs.getInt("ImportId"));
                importList.setImportDate(rs.getDate("ImportDate"));
                importList.setImportedBy(rs.getInt("ImportedBy"));
                importList.setImportTypeId(rs.getInt("ImportTypeId"));
                importList.setNote(rs.getString("Note"));
                importList.setImportedByName(rs.getString("ImportedByName"));
                importList.setImportTypeName(rs.getString("ImportTypeName"));
                
                list.add(importList);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in getRelatedImportsByRequestId: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy chi tiết import theo import ID
    public List<ImportDetail> getImportDetailsByImportId(int importId) {
        List<ImportDetail> list = new ArrayList<>();
        String sql = "SELECT id.*, m.MaterialName " +
                    "FROM ImportDetail id " +
                    "LEFT JOIN Materials m ON id.MaterialId = m.MaterialId " +
                    "WHERE id.ImportId = ?";
        
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, importId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ImportDetail detail = new ImportDetail();
                detail.setImportDetailId(rs.getInt("ImportDetailId"));
                detail.setImportId(rs.getInt("ImportId"));
                detail.setMaterialId(rs.getInt("MaterialId"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPrice(rs.getDouble("Price"));
                detail.setMaterialName(rs.getString("MaterialName"));
                
                list.add(detail);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in getImportDetailsByImportId: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy chi tiết import liên quan đến request
    public List<ImportDetail> getRelatedImportDetailsByRequestId(int requestId) {
        List<ImportDetail> list = new ArrayList<>();
        String sql = "SELECT id.*, m.MaterialName, il.ImportDate " +
                    "FROM ImportDetail id " +
                    "LEFT JOIN Materials m ON id.MaterialId = m.MaterialId " +
                    "LEFT JOIN ImportList il ON id.ImportId = il.ImportId " +
                    "WHERE id.MaterialId IN (" +
                    "    SELECT rd.MaterialId FROM RequestDetail rd WHERE rd.RequestId = ?" +
                    ") ORDER BY il.ImportDate DESC, id.ImportDetailId";
        
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ImportDetail detail = new ImportDetail();
                detail.setImportDetailId(rs.getInt("ImportDetailId"));
                detail.setImportId(rs.getInt("ImportId"));
                detail.setMaterialId(rs.getInt("MaterialId"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPrice(rs.getDouble("Price"));
                detail.setMaterialName(rs.getString("MaterialName"));
                
                list.add(detail);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in getRelatedImportDetailsByRequestId: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
