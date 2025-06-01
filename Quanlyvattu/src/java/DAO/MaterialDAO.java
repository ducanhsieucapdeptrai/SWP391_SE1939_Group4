package DAO;

import dal.DBContext;
import java.sql.*;
import java.util.*;
import model.Material;

public class MaterialDAO extends DBContext {

    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT * FROM Materials";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setSubCategoryId(rs.getInt("SubCategoryId"));
                m.setStatusId(rs.getInt("StatusId"));
                m.setImage(rs.getString("Image"));
                m.setDescription(rs.getString("Description"));
                m.setQuantity(rs.getInt("Quantity"));
                m.setMinQuantity(rs.getInt("MinQuantity"));
                m.setPrice(rs.getDouble("Price"));
                m.setCreatedAt(rs.getTimestamp("CreatedAt"));
                m.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ Lấy vật tư phân trang
    public List<Material> getMaterialsByPage(int offset, int pageSize) {
        List<Material> list = new ArrayList<>();
        String sql = """
    SELECT m.*, s.StatusName, sc.SubCategoryName, c.CategoryName
    FROM Materials m
    JOIN MaterialStatus s ON m.StatusId = s.StatusId
    JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
    JOIN Categories c ON sc.CategoryId = c.CategoryId
    ORDER BY m.MaterialId
    LIMIT ? OFFSET ?
""";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, pageSize);
            st.setInt(2, offset);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setSubCategoryId(rs.getInt("SubCategoryId"));
                m.setStatusId(rs.getInt("StatusId"));
                m.setImage(rs.getString("Image"));
                m.setDescription(rs.getString("Description"));
                m.setQuantity(rs.getInt("Quantity"));
                m.setMinQuantity(rs.getInt("MinQuantity"));
                m.setPrice(rs.getDouble("Price"));
                m.setCreatedAt(rs.getTimestamp("CreatedAt"));
                m.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
                m.setStatusName(rs.getString("StatusName"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                m.setCategoryName(rs.getString("CategoryName"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ Lấy tổng số lượng vật tư
    public int getTotalMaterialCount() {
        String sql = "SELECT COUNT(*) FROM Materials";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Material> searchMaterials(String category, String subcategory, String name) {
    List<Material> list = new ArrayList<>();

    String sql = """
        SELECT m.*, c.CategoryName, sc.SubCategoryName, s.StatusName
        FROM Materials m
        JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
        JOIN Categories c ON sc.CategoryId = c.CategoryId
        JOIN MaterialStatus s ON m.StatusId = s.StatusId
        WHERE 1=1
    """;

    if (category != null && !category.isEmpty()) {
        sql += " AND c.CategoryName LIKE ?";
    }
    if (subcategory != null && !subcategory.isEmpty()) {
        sql += " AND sc.SubCategoryName LIKE ?";
    }
    if (name != null && !name.isEmpty()) {
        sql += " AND m.MaterialName LIKE ?";
    }

    try {
        PreparedStatement ps = connection.prepareStatement(sql);

        int idx = 1;
        if (category != null && !category.isEmpty()) {
            ps.setString(idx++, "%" + category + "%");
        }
        if (subcategory != null && !subcategory.isEmpty()) {
            ps.setString(idx++, "%" + subcategory + "%");
        }
        if (name != null && !name.isEmpty()) {
            ps.setString(idx++, "%" + name + "%");
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Material m = new Material();
            m.setMaterialId(rs.getInt("MaterialId"));
            m.setMaterialName(rs.getString("MaterialName"));
            m.setSubCategoryId(rs.getInt("SubCategoryId"));
            m.setStatusId(rs.getInt("StatusId"));
            m.setImage(rs.getString("Image"));
            m.setDescription(rs.getString("Description"));
            m.setQuantity(rs.getInt("Quantity"));
            m.setMinQuantity(rs.getInt("MinQuantity"));
            m.setPrice(rs.getDouble("Price"));
            m.setCreatedAt(rs.getTimestamp("CreatedAt"));
            m.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
            m.setCategoryName(rs.getString("CategoryName"));
            m.setSubCategoryName(rs.getString("SubCategoryName"));
            m.setStatusName(rs.getString("StatusName"));
            list.add(m);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
    

    // Có thể thêm: insertMaterial(), updateMaterial(), deleteMaterialById(), searchMaterialByName()
}
