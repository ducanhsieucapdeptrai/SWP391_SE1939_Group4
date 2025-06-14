package DAO;

import dal.DBContext;
import java.sql.*;
import java.util.*;
import model.Material;

public class MaterialDAO  extends DBContext{
    
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
    // ✅ Lấy một vật tư theo ID

    public Material getMaterialById(int id) {
        String sql = """
        SELECT m.*, s.StatusName, sc.SubCategoryName, c.CategoryName
        FROM Materials m
        JOIN MaterialStatus s ON m.StatusId = s.StatusId
        JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
        JOIN Categories c ON sc.CategoryId = c.CategoryId
        WHERE m.MaterialId = ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
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
                return m;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateMaterial(Material m) {
        String sql = """
        UPDATE Materials
        SET MaterialName = ?, SubCategoryId = ?, StatusId = ?, Image = ?, 
            Description = ?, Quantity = ?, MinQuantity = ?, Price = ?, 
            UpdatedAt = CURRENT_TIMESTAMP
        WHERE MaterialId = ?
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, m.getMaterialName());
            st.setInt(2, m.getSubCategoryId());
            st.setInt(3, m.getStatusId());
            st.setString(4, m.getImage());
            st.setString(5, m.getDescription());
            st.setInt(6, m.getQuantity());
            st.setInt(7, m.getMinQuantity());
            st.setDouble(8, m.getPrice());
            st.setInt(9, m.getMaterialId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Helper method to close database resources
     */
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }

    
    
    // ✅ Add new material with proper connection handling and return generated ID
    public boolean addMaterial(Material material) {
        String sql = """
            INSERT INTO Materials 
            (MaterialName, SubCategoryId, StatusId, Image, Description, 
            Quantity, MinQuantity, Price, CreatedAt, UpdatedAt) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """;

        try (
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, material.getMaterialName());
            ps.setInt(2, material.getSubCategoryId());
            ps.setInt(3, material.getStatusId());
            ps.setString(4, material.getImage());
            ps.setString(5, material.getDescription());
            ps.setInt(6, material.getQuantity());
            ps.setInt(7, material.getMinQuantity());
            ps.setDouble(8, material.getPrice());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        material.setMaterialId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    

    public List<Material> getRecentImportMaterials() {
        List<Material> imports = new ArrayList<>();
        String sql = """
            SELECT m.MaterialId, m.MaterialName, id.Quantity as ImportQuantity, 
                   i.ImportDate, c.CategoryName, sc.SubCategoryName
            FROM ImportDetail id
            JOIN Materials m ON id.MaterialId = m.MaterialId
            JOIN ImportList i ON id.ImportId = i.ImportId
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            ORDER BY i.ImportDate DESC
            LIMIT 5
            """;
            
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connection;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setImportQuantity(rs.getInt("ImportQuantity"));
                m.setImportDate(rs.getTimestamp("ImportDate"));
                m.setCategoryName(rs.getString("CategoryName"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                imports.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error in getRecentImportMaterials: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return imports;
    }
    
    public List<Material> getRecentExportMaterials() {
        List<Material> exports = new ArrayList<>();
        String sql = """
            SELECT m.MaterialId, m.MaterialName, ed.Quantity as ExportQuantity, 
                   e.ExportDate, c.CategoryName, sc.SubCategoryName
            FROM ExportDetail ed
            JOIN Materials m ON ed.MaterialId = m.MaterialId
            JOIN ExportList e ON ed.ExportId = e.ExportId
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            ORDER BY e.ExportDate DESC
            LIMIT 5
            """;
            
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connection;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setExportQuantity(rs.getInt("ExportQuantity"));
                m.setExportDate(rs.getTimestamp("ExportDate"));
                m.setCategoryName(rs.getString("CategoryName"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                exports.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error in getRecentExportMaterials: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return exports;
    }
    
   
    
    

    // Có thể thêm: deleteMaterialById()
}
