package DAO;

import dal.DBContext;
import model.MaterialStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialStatusDAO extends DBContext {

    // Get all material statuses
    public List<MaterialStatus> getAllStatuses() throws SQLException {
        List<MaterialStatus> statuses = new ArrayList<>();
        String sql = "SELECT * FROM MaterialStatus ORDER BY StatusName";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                MaterialStatus status = new MaterialStatus();
                status.setStatusId(rs.getInt("StatusId"));
                status.setStatusName(rs.getString("StatusName"));
                // MaterialStatus table doesn't have a Description column
                status.setDescription("");
                statuses.add(status);
            }
        }

        return statuses;
    }

    // Get status by ID
    public MaterialStatus getStatusById(int statusId) throws SQLException {
        String sql = "SELECT * FROM MaterialStatus WHERE StatusId = ?";

        try (Connection conn = getNewConnection(); PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, statusId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    MaterialStatus status = new MaterialStatus();
                    status.setStatusId(rs.getInt("StatusId"));
                    status.setStatusName(rs.getString("StatusName"));
                    // MaterialStatus table doesn't have a Description column
                    status.setDescription("");
                    return status;
                }
            }
        }

        return null;
    }

    // Add other CRUD operations as needed
    // (create, update, delete methods would go here)
}
