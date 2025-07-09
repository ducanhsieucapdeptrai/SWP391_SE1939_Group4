package DAO;

import dal.DBContext;
import java.sql.*;

public class ExportDetailDAO extends DBContext {

    // Thêm 1 dòng chi tiết xuất kho
    public boolean insertExportDetail(int exportId, int materialId, int quantity) {
        String sql = """
            INSERT INTO ExportDetail (ExportId, MaterialId, Quantity)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, exportId);
            ps.setInt(2, materialId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
