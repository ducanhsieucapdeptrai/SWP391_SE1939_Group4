package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dal.DBContext;

public class HomepageDAO {

    private Connection conn;

    public HomepageDAO() {
        try {
            conn = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalMaterialCategory() {
        String sql = "SELECT COUNT(*) FROM SubCategories";
        return getIntResult(sql);
    }

    public int getTotalImport() {
        String sql = "SELECT SUM(quantity) FROM ImportDetail";
        return getIntResult(sql);
    }

    public int getTotalExport() {
        String sql = "SELECT SUM(quantity) FROM ExportDetail";
        return getIntResult(sql);
    }

    public int getPendingRequests() {
        String sql = "SELECT COUNT(*) FROM RequestList WHERE status = 'Pending'";
        return getIntResult(sql);
    }

    private int getIntResult(String sql) {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
