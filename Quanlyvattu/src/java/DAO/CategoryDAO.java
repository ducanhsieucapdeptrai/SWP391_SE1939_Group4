package DAO;

import dal.DBContext;
import model.Category;
import model.SubCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private final DBContext dbContext = new DBContext();

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT CategoryId, CategoryName FROM Categories ORDER BY CategoryName";
        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("CategoryId"));
                category.setCategoryName(rs.getString("CategoryName"));
                categories.add(category);
            }
        }

        return categories;
    }

    public List<Category> getCategories(String sortBy, String searchTerm) throws SQLException {
        List<Category> categories = new ArrayList<>();
        String validSortColumns = "CategoryId, CategoryName";

        if (sortBy == null || !validSortColumns.contains(sortBy)) {
            sortBy = "CategoryId";
        }

        StringBuilder sql = new StringBuilder("SELECT CategoryId, CategoryName FROM Categories ");
        boolean hasSearchTerm = searchTerm != null && !searchTerm.trim().isEmpty();

        if (hasSearchTerm) {
            sql.append("WHERE CategoryName LIKE ? ");
        }

        sql.append("ORDER BY ").append(sortBy).append(" ASC");

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (hasSearchTerm) {
                stmt.setString(1, "%" + searchTerm.trim() + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category();
                    category.setCategoryId(rs.getInt("CategoryId"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    categories.add(category);
                }
            }
        }

        return categories;
    }

    public boolean addCategory(Category category) {
        if (category == null) {
            return false;
        }

        String sql = "INSERT INTO Categories (CategoryName) VALUES (?)";

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCategory(Category category) {
        if (category == null) {
            return false;
        }

        String sql = "UPDATE Categories SET CategoryName = ? WHERE CategoryId = ?";

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setInt(2, category.getCategoryId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCategory(int id) {
        String checkSql = "SELECT COUNT(*) FROM SubCategories WHERE CategoryId = ?";
        String deleteSql = "DELETE FROM Categories WHERE CategoryId = ?";

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, id);
                return deleteStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Category getCategoryById(int id) {
        String sql = "SELECT CategoryId, CategoryName FROM Categories WHERE CategoryId = ?";

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setCategoryId(rs.getInt("CategoryId"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    return category;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<SubCategory> getAllSubCategories() throws SQLException {
        List<SubCategory> subCategories = new ArrayList<>();
        String sql = """
            SELECT sc.*, c.CategoryName 
            FROM SubCategories sc
            JOIN Categories c ON sc.CategoryId = c.CategoryId
            ORDER BY c.CategoryName, sc.SubCategoryName
        """;

        try (Connection conn = dbContext.getNewConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setSubCategoryId(rs.getInt("SubCategoryId"));
                subCategory.setSubCategoryName(rs.getString("SubCategoryName"));
                subCategory.setCategoryId(rs.getInt("CategoryId"));
                subCategory.setCategoryName(rs.getString("CategoryName"));

                subCategories.add(subCategory);
            }
        }

        return subCategories;
    }
}
