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

    public Material getMaterialById(String materialId) {
        Material material = null;
        try {
            String sql = "SELECT * FROM Material WHERE materialId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                material = new Material();
                material.setMaterialId(rs.getInt("materialId"));
                material.setMaterialName(rs.getString("materialName"));
                material.setImage(rs.getString("image"));
                material.setPrice(rs.getDouble("price"));
                material.setQuantity(rs.getInt("quantity"));
                material.setMinQuantity(rs.getInt("minQuantity"));
                material.setCreatedAt(rs.getTimestamp("createdAt"));
                material.setUpdatedAt(rs.getTimestamp("updatedAt"));
                material.setStatusName(rs.getString("status"));
                material.setCategoryName(rs.getString("categoryName"));
                material.setSubCategoryName(rs.getString("subCategoryName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return material;
    }

    // Có thể thêm: insertMaterial(), updateMaterial(), deleteMaterialById(), searchMaterialByName()
    public Material getMaterialById(int materialId) {
        Material material = null;
        try {
            String sql = "SELECT m.*, c.categoryName, sc.subCategoryName, s.statusName "
                    + "FROM materials m "
                    + "JOIN subcategories sc ON m.subCategoryId = sc.subCategoryId "
                    + "JOIN categories c ON sc.categoryId = c.categoryId "
                    + "JOIN materialstatus s ON m.statusId = s.statusId "
                    + "WHERE m.materialId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                material = new Material();
                material.setMaterialId(rs.getInt("materialId"));
                material.setMaterialName(rs.getString("materialName"));
                material.setImage(rs.getString("image"));
                material.setPrice(rs.getDouble("price"));
                material.setQuantity(rs.getInt("quantity"));
                material.setMinQuantity(rs.getInt("minQuantity"));
                material.setDescription(rs.getString("description"));
                material.setCreatedAt(rs.getTimestamp("createdAt"));
                material.setUpdatedAt(rs.getTimestamp("updatedAt"));
                material.setStatusId(rs.getInt("statusId"));
                material.setSubCategoryId(rs.getInt("subCategoryId"));
                material.setStatusName(rs.getString("statusName"));
                material.setCategoryName(rs.getString("categoryName"));
                material.setSubCategoryName(rs.getString("subCategoryName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return material;
    }

}
