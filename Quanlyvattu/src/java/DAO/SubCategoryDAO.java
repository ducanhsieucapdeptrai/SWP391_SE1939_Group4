package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.SubCategory;

public class SubCategoryDAO extends DBContext {

    public List<SubCategory> getAllWithCategory() {
        List<SubCategory> list = new ArrayList<>();
        String sql = "SELECT s.SubCategoryId, s.SubCategoryName, s.CategoryId, c.CategoryName "
                + "FROM SubCategories s JOIN Categories c ON s.CategoryId = c.CategoryId";

        try (
                Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SubCategory s = new SubCategory();
                s.setSubCategoryId(rs.getInt("SubCategoryId"));
                s.setSubCategoryName(rs.getString("SubCategoryName"));
                s.setCategoryId(rs.getInt("CategoryId")); // 👈 PHẢI có dòng này để gán CategoryId
                s.setCategoryName(rs.getString("CategoryName"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
