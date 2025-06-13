package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.RequestDetail;

public class RequestDetailDAO {

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
            System.out.println("Error in RequestDetailDAO: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}