package DAO;

import dal.DBContext;
import model.Material;
import model.RequestDetail;
import model.RequestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportPurchaseRequestDAO extends DBContext {

    public List<RequestType> getAllRequestTypes() {
        List<RequestType> list = new ArrayList<>();
        String sql = "SELECT RequestTypeId, RequestTypeName FROM RequestType WHERE RequestTypeId IN (2,3)";
        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new RequestType(rs.getInt("RequestTypeId"), rs.getString("RequestTypeName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Material> getAllMaterials() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT MaterialId, MaterialName, Image FROM Materials WHERE StatusId = 1";
        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setImage(rs.getString("Image"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int createRequest(int userId, int typeId, String note, List<RequestDetail> details) {
        String sqlReq = "INSERT INTO RequestList (RequestedBy, RequestTypeId, Note, Status, RequestDate) VALUES (?, ?, ?, 'Pending', CURRENT_TIMESTAMP)";
        String sqlDetail = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";
        int requestId = -1;

        try (Connection conn = getNewConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(sqlReq, Statement.RETURN_GENERATED_KEYS)) {
                st.setInt(1, userId);
                st.setInt(2, typeId);
                st.setString(3, note);
                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    requestId = rs.getInt(1);
                    try (PreparedStatement stDetail = conn.prepareStatement(sqlDetail)) {
                        for (RequestDetail d : details) {
                            stDetail.setInt(1, requestId);
                            stDetail.setInt(2, d.getMaterialId());
                            stDetail.setInt(3, d.getQuantity());
                            stDetail.addBatch();
                        }
                        stDetail.executeBatch();
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
        }
        return requestId;
    }
}
