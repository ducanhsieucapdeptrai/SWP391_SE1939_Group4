package DAO;

import dal.DBContext;
import model.Material;
import model.RequestDetail;
import model.RequestType;

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

    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = """
            SELECT MaterialId, MaterialName, SubCategoryId, StatusId,
                   Image, Description, Quantity, Unit
            FROM Materials
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
                m.setUnit(rs.getString("Unit"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int createRequest(int userId, int typeId, Integer subTypeId, String note,
            List<RequestDetail> details, Integer projectId) {

        String sqlRequest = """
            INSERT INTO RequestList 
            (RequestedBy, RequestTypeId, Note, Status, RequestDate, ProjectId) 
            VALUES (?, ?, ?, 'Pending', CURRENT_TIMESTAMP, ?)
        """;

        String sqlDetail = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";

        int requestId = -1;

        try (Connection conn = getNewConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stReq = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS)) {
                stReq.setInt(1, userId);
                stReq.setInt(2, typeId);
                stReq.setString(3, note);

                if (projectId != null) {
                    stReq.setInt(4, projectId);
                } else {
                    stReq.setNull(4, Types.INTEGER);
                }

                stReq.executeUpdate();

                try (ResultSet rs = stReq.getGeneratedKeys()) {
                    if (rs.next()) {
                        requestId = rs.getInt(1);

                        try (PreparedStatement stDet = conn.prepareStatement(sqlDetail)) {
                            for (RequestDetail d : details) {
                                stDet.setInt(1, requestId);
                                stDet.setInt(2, d.getMaterialId());
                                stDet.setInt(3, d.getQuantity());
                                stDet.addBatch();
                            }
                            stDet.executeBatch();
                        }
                    }
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return requestId;
    }
}
