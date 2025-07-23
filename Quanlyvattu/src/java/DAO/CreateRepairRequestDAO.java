// CreateRepairRequestDAO.java
package DAO;

import dal.DBContext;
import model.Material;
import model.RequestDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreateRepairRequestDAO extends DBContext {

    public int createRepairRequest(int userId, int typeId, String note, List<RequestDetail> details, Integer projectId) throws Exception {
        int requestId = -1;

        String insertRequestSQL = "INSERT INTO RequestList (RequestedBy, RequestTypeId, Note, ProjectId) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(insertRequestSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setInt(2, typeId);
            ps.setString(3, note);
            if (projectId != null) {
                ps.setInt(4, projectId);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    requestId = rs.getInt(1);
                }
            }

            if (requestId > 0 && details != null && !details.isEmpty()) {
                String insertDetailSQL = "INSERT INTO RequestDetail (RequestId, MaterialId, Quantity) VALUES (?, ?, ?)";
                try (PreparedStatement psDetail = conn.prepareStatement(insertDetailSQL)) {
                    for (RequestDetail detail : details) {
                        psDetail.setInt(1, requestId);
                        psDetail.setInt(2, detail.getMaterialId());
                        psDetail.setInt(3, detail.getQuantity());
                        psDetail.addBatch();
                    }
                    psDetail.executeBatch();
                }
            }
        }

        return requestId;
    }

    public List<Material> getAllMaterials() throws Exception {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT MaterialId, MaterialName, Image, Quantity FROM Materials";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("MaterialId"));
                m.setMaterialName(rs.getString("MaterialName"));
                m.setImage(rs.getString("Image"));
                m.setQuantity(rs.getInt("Quantity"));
                list.add(m);
            }
        }

        return list;
    }
}
