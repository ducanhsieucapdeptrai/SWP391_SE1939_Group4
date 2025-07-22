package DAO;

import dal.DBContext;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreateRequestExport_PurcharDAO extends DBContext {

    public List<RequestType> getAllRequestTypes() {
        List<RequestType> list = new ArrayList<>();
        String sql = "SELECT RequestTypeId, RequestTypeName FROM RequestType";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                RequestType type = new RequestType();
                type.setRequestTypeId(rs.getInt("RequestTypeId"));
                type.setRequestTypeName(rs.getString("RequestTypeName"));
                list.add(type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<RequestSubType> getAllRequestSubtypes() {
        List<RequestSubType> list = new ArrayList<>();
        String sql = "SELECT SubTypeId, RequestTypeId, SubTypeName FROM RequestSubType";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                RequestSubType subType = new RequestSubType();
                subType.setSubTypeId(rs.getInt("SubTypeId"));
                subType.setRequestTypeId(rs.getInt("RequestTypeId"));
                subType.setSubTypeName(rs.getString("SubTypeName"));
                list.add(subType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT CategoryId, CategoryName FROM Categories";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("CategoryId"));
                cat.setCategoryName(rs.getString("CategoryName"));
                list.add(cat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<SubCategory> getSubCategoriesByCategoryId(int categoryId) {
        List<SubCategory> list = new ArrayList<>();
        String sql = "SELECT SubCategoryId, SubCategoryName FROM SubCategories WHERE CategoryId = ?";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, categoryId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    SubCategory subCat = new SubCategory();
                    subCat.setSubCategoryId(rs.getInt("SubCategoryId"));
                    subCat.setSubCategoryName(rs.getString("SubCategoryName"));
                    subCat.setCategoryId(categoryId);
                    list.add(subCat);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Material> getMaterialsBySubCategoryId(int subCatId) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT MaterialId, MaterialName, Image, Quantity, MinQuantity FROM Materials WHERE SubCategoryId = ?";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, subCatId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Material m = new Material();
                    m.setMaterialId(rs.getInt("MaterialId"));
                    m.setMaterialName(rs.getString("MaterialName"));
                    m.setImage(rs.getString("Image"));
                    m.setQuantity(rs.getInt("Quantity"));
                    m.setMinQuantity(rs.getInt("MinQuantity"));
                    list.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Material> getMaterialsByCategoryId(int catId) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT m.MaterialId, m.MaterialName, m.Image, m.Quantity, m.MinQuantity "
                + "FROM Materials m JOIN SubCategories s ON m.SubCategoryId = s.SubCategoryId "
                + "WHERE s.CategoryId = ?";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, catId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Material m = new Material();
                    m.setMaterialId(rs.getInt("MaterialId"));
                    m.setMaterialName(rs.getString("MaterialName"));
                    m.setImage(rs.getString("Image"));
                    m.setQuantity(rs.getInt("Quantity"));
                    m.setMinQuantity(rs.getInt("MinQuantity"));
                    list.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int createRequest(int userId, int typeId, Integer subTypeId, String note,
            List<RequestDetail> details, Integer projectId) {

        String sqlRequest = "INSERT INTO RequestList (RequestedBy, RequestTypeId, SubTypeId, Note, Status, RequestDate, ProjectId) "
                + "VALUES (?, ?, ?, ?, 'Pending', CURRENT_TIMESTAMP, ?)";
        String sqlDetail = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";

        int requestId = -1;

        try (Connection conn = getNewConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stReq = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS)) {
                stReq.setInt(1, userId);
                stReq.setInt(2, typeId);

                if (subTypeId != null) {
                    stReq.setInt(3, subTypeId);
                } else {
                    stReq.setNull(3, java.sql.Types.INTEGER);
                }

                stReq.setString(4, note);

                if (projectId != null) {
                    stReq.setInt(5, projectId);
                } else {
                    stReq.setNull(5, java.sql.Types.INTEGER);
                }

                stReq.executeUpdate();

                try (ResultSet rs = stReq.getGeneratedKeys()) {
                    if (rs.next()) {
                        requestId = rs.getInt(1);

                        try (PreparedStatement stDet = conn.prepareStatement(sqlDetail)) {
                            for (RequestDetail detail : details) {
                                stDet.setInt(1, requestId);
                                stDet.setInt(2, detail.getMaterialId());
                                stDet.setInt(3, detail.getQuantity());
                                stDet.addBatch();
                            }

                            stDet.executeBatch();
                        }
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return requestId;
    }
}
