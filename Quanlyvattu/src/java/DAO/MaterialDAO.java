package DAO;

import dal.DBContext;
import java.sql.*;
import java.util.*;
import model.Category;
import model.Material;
import model.SubCategory;

public class MaterialDAO extends DBContext {

    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = """
            SELECT m.MaterialId, m.MaterialName, m.SubCategoryId, m.StatusId, 
                   m.Image, m.Description, m.Quantity, 
                   m.CreateAt, m.Last_updated,
                   sc.CategoryId, sc.SubCategoryName,
                   c.CategoryName, 
                   s.StatusName,
                   u.Name AS UnitName
            FROM Materials m
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            JOIN MaterialStatus s ON m.StatusId = s.StatusId
            JOIN Units u ON m.Unit_id = u.Unit_id
        """;

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setSubCategoryId(rs.getInt("SubCategoryId"));
                m.setStatusId(rs.getInt("StatusId"));
                m.setImage(rs.getString("Image"));
                m.setDescription(rs.getString("Description"));
                m.setQuantity(rs.getInt("Quantity"));
                m.setCreatedAt(rs.getDate("CreateAt") != null ? new Timestamp(rs.getDate("CreateAt").getTime()) : null);

                m.setUpdatedAt(rs.getTimestamp("Last_updated"));
                m.setUpdatedAt(rs.getTimestamp("Last_updated"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                m.setCategoryName(rs.getString("CategoryName"));
                m.setStatusName(rs.getString("StatusName"));
                m.setCategoryId(rs.getInt("CategoryId"));
                m.setUnitName(rs.getString("UnitName"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Material> getMaterialsByPage(int offset, int pageSize) {
        List<Material> list = new ArrayList<>();
        String sql = """
            SELECT m.MaterialId, m.MaterialName, m.Quantity, m.Image,
                   u.Name AS UnitName,
                   sc.SubCategoryId, sc.SubCategoryName,
                   c.CategoryId, c.CategoryName
            FROM Materials m
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            JOIN Units u ON m.Unit_id = u.Unit_id
            ORDER BY m.MaterialId
            LIMIT ? OFFSET ?
        """;

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, pageSize);
            st.setInt(2, offset);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setQuantity(rs.getInt("Quantity"));
                m.setImage(rs.getString("Image"));
                m.setUnitName(rs.getString("UnitName"));
                m.setSubCategoryId(rs.getInt("SubCategoryId"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                m.setCategoryId(rs.getInt("CategoryId"));
                m.setCategoryName(rs.getString("CategoryName"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalMaterialCount() {
        String sql = "SELECT COUNT(*) FROM Materials";
        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
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
            SELECT m.*, c.CategoryName, sc.SubCategoryName, s.StatusName, u.Name AS UnitName
            FROM Materials m
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            JOIN MaterialStatus s ON m.StatusId = s.StatusId
            JOIN Units u ON m.Unit_id = u.Unit_id
            WHERE 1=1
        """;

        if (category != null && !category.isEmpty()) {
            sql += " AND c.CategoryId = ?";
        }
        if (subcategory != null && !subcategory.isEmpty()) {
            sql += " AND sc.SubCategoryId = ?";
        }
        if (name != null && !name.isEmpty()) {
            sql += " AND m.MaterialName LIKE ?";
        }

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (category != null && !category.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(category));
            }
            if (subcategory != null && !subcategory.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(subcategory));
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
                m.setCreatedAt(rs.getDate("CreateAt") != null ? new Timestamp(rs.getDate("CreateAt").getTime()) : null);
                m.setUpdatedAt(rs.getTimestamp("Last_updated"));
                m.setCategoryName(rs.getString("CategoryName"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                m.setStatusName(rs.getString("StatusName"));
                m.setUnitName(rs.getString("UnitName"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Material getMaterialById(int id) {
        String sql = """
            SELECT m.MaterialId, m.MaterialName, m.SubCategoryId, m.StatusId, 
                   m.Image, m.Description, m.Quantity, m.CreateAt, m.Last_updated,
                   sc.CategoryId, sc.SubCategoryName,
                   c.CategoryName, 
                   s.StatusName,
                   u.Unit_id, u.Name AS UnitName
            FROM Materials m
            JOIN SubCategories sc ON m.SubCategoryId = sc.SubCategoryId
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            JOIN MaterialStatus s ON m.StatusId = s.StatusId
            JOIN Units u ON m.Unit_id = u.Unit_id
            WHERE m.MaterialId = ?
        """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
                m.setCreatedAt(rs.getDate("CreateAt") != null ? new Timestamp(rs.getDate("CreateAt").getTime()) : null);
                m.setUpdatedAt(rs.getTimestamp("Last_updated"));
                m.setSubCategoryName(rs.getString("SubCategoryName"));
                m.setCategoryName(rs.getString("CategoryName"));
                m.setStatusName(rs.getString("StatusName"));
                m.setCategoryId(rs.getInt("CategoryId"));
                m.setUnitId(rs.getInt("Unit_id"));
                m.setUnitName(rs.getString("UnitName"));
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addMaterial(Material material) {
        String sql = """
            INSERT INTO Materials 
            (MaterialName, SubCategoryId, StatusId, Image, Description, 
             Quantity, Unit_id, CreateAt) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, material.getMaterialName());
            ps.setInt(2, material.getSubCategoryId());
            ps.setInt(3, material.getStatusId());
            ps.setString(4, material.getImage());
            ps.setString(5, material.getDescription());
            ps.setInt(6, material.getQuantity());
            ps.setInt(7, material.getUnitId());
            ps.setDate(8, new java.sql.Date(material.getCreatedAt().getTime())); // Set CreateAt

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        material.setMaterialId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMaterial(Material m) {
        String sql = """
            UPDATE Materials
            SET MaterialName = ?, SubCategoryId = ?, StatusId = ?, Image = ?, 
                Description = ?, Quantity = ?, Unit_id = ?, 
                Last_updated = CURRENT_TIMESTAMP
            WHERE MaterialId = ?
        """;

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, m.getMaterialName());
            st.setInt(2, m.getSubCategoryId());
            st.setInt(3, m.getStatusId());
            st.setString(4, m.getImage());
            st.setString(5, m.getDescription());
            st.setInt(6, m.getQuantity());
            st.setInt(7, m.getUnitId());
            st.setInt(8, m.getMaterialId());

            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addMaterial(Material material) {
        String sql = """
            INSERT INTO Materials 
            (MaterialName, SubCategoryId, StatusId, Image, Description, 
             Quantity, Unit) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, material.getMaterialName());
            ps.setInt(2, material.getSubCategoryId());
            ps.setInt(3, material.getStatusId());
            ps.setString(4, material.getImage());
            ps.setString(5, material.getDescription());
            ps.setInt(6, material.getQuantity());
            ps.setString(7, material.getUnit());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        material.setMaterialId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT CategoryId, CategoryName FROM Categories ORDER BY CategoryName";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("CategoryId"));
                c.setCategoryName(rs.getString("CategoryName"));
                categories.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<SubCategory> getAllSubcategories() {
        List<SubCategory> subcategories = new ArrayList<>();
        String sql = "SELECT SubCategoryId, SubCategoryName, CategoryId FROM SubCategories ORDER BY SubCategoryName";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SubCategory sub = new SubCategory();
                sub.setSubCategoryId(rs.getInt("SubCategoryId"));
                sub.setSubCategoryName(rs.getString("SubCategoryName"));
                sub.setCategoryId(rs.getInt("CategoryId"));
                subcategories.add(sub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subcategories;
    }

    public void addSubCategory(int categoryId, String subCategoryName) {
        String sql = "INSERT INTO SubCategories (SubCategoryName, CategoryId) VALUES (?, ?)";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subCategoryName);
            ps.setInt(2, categoryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(String name) {
        String sql = "INSERT INTO Categories (CategoryName) VALUES (?)";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMaterialQuantity(int materialId, int quantity) throws SQLException {
        String sql = "UPDATE Materials SET Quantity = ?, Last_updated = NOW() WHERE MaterialId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, materialId);
            ps.executeUpdate();
        }
    }

    public boolean updateMaterialBasicInfo(Material material) throws SQLException {
        String sql = """
        UPDATE Materials 
        SET MaterialName = ?, 
            SubCategoryId = ?, 
            Unit_id = ?, 
            Description = ?, 
            Last_updated = NOW() 
        WHERE MaterialId = ?
    """;
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getMaterialName());
            ps.setInt(2, material.getSubCategoryId());
            ps.setInt(3, material.getUnitId());
            ps.setString(4, material.getDescription());
            ps.setInt(5, material.getMaterialId());
            return ps.executeUpdate() > 0;
        }
    }
}
