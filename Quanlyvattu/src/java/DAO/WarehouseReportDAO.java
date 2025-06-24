package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class WarehouseReportDAO {

    
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
    
   
    public List<ImportList> getRelatedImportsByRequestId(int requestId) {
        List<ImportList> list = new ArrayList<>();
        String sql = "SELECT il.*, u.FullName as ImportedByName, it.ImportTypeName " +
        "FROM ImportList il " +
        "LEFT JOIN Users u ON il.ImportedBy = u.UserId " +
        "LEFT JOIN ImportType it ON il.ImportTypeId = it.ImportTypeId " +
        "WHERE il.RequestId = ? " +
        "ORDER BY il.ImportDate DESC";
        
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
    
  
    public List<ImportDetail> getRelatedImportDetailsByRequestId(int requestId) {
        List<ImportDetail> list = new ArrayList<>();
        String sql = "SELECT id.*, m.MaterialName " +
      "FROM ImportDetail id " +
      "LEFT JOIN Materials m ON id.MaterialId = m.MaterialId " +
      "LEFT JOIN ImportList il ON id.ImportId = il.ImportId " +
      "WHERE il.RequestId = ?  AND id.Quantity > 0 " + 
      "ORDER BY il.ImportDate DESC, id.ImportDetailId";
        
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
    public List<ExportList> getRelatedExportsByRequestId(int requestId) {
    List<ExportList> list = new ArrayList<>();
    String sql =
        "SELECT el.*, u.FullName as ExportedByName, et.ExportTypeName " +
        "FROM ExportList el " +
        "LEFT JOIN Users u ON el.ExportedBy = u.UserId " +
        "LEFT JOIN ExportType et ON el.ExportTypeId = et.ExportTypeId " +
        "WHERE el.RequestId = ? " +
        "ORDER BY el.ExportDate DESC";
    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, requestId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ExportList ex = new ExportList();
                ex.setExportId(rs.getInt("ExportId"));
                ex.setExportDate(rs.getTimestamp("ExportDate"));
                ex.setExportedBy(rs.getInt("ExportedBy"));
                ex.setExportTypeId(rs.getInt("ExportTypeId"));
                ex.setNote(rs.getString("Note"));
                ex.setExportedByName(rs.getString("ExportedByName"));
                ex.setExportTypeName(rs.getString("ExportTypeName"));
                list.add(ex);
            }
        }
    } catch (Exception e) {
        System.out.println("Error in getRelatedExportsByRequestId: " + e.getMessage());
        e.printStackTrace();
    }
    return list;
}


public List<ExportDetail> getRelatedExportDetailsByRequestId(int requestId) {
    List<ExportDetail> list = new ArrayList<>();
    String sql =
        "SELECT ed.*, m.MaterialName " +
        "FROM ExportDetail ed " +
        "LEFT JOIN Materials m ON ed.MaterialId = m.MaterialId " +
        "LEFT JOIN ExportList el ON ed.ExportId = el.ExportId " +
        "WHERE el.RequestId = ? AND ed.Quantity > 0 " + 
        "ORDER BY el.ExportDate DESC, ed.ExportDetailId";
    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, requestId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ExportDetail det = new ExportDetail();
                det.setExportDetailId(rs.getInt("ExportDetailId"));
                det.setExportId(rs.getInt("ExportId"));
                det.setMaterialId(rs.getInt("MaterialId"));
                det.setQuantity(rs.getInt("Quantity"));
                det.setMaterialName(rs.getString("MaterialName"));
                list.add(det);
            }
        }
    } catch (Exception e) {
        System.out.println("Error in getRelatedExportDetailsByRequestId: " + e.getMessage());
        e.printStackTrace();
    }
    return list;
}
}
