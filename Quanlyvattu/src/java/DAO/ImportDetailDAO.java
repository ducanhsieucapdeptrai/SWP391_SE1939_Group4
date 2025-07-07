package DAO;

import dal.DBContext;
import java.sql.*;

public class ImportDetailDAO extends DBContext {

    // Thêm 1 dòng chi tiết nhập kho
    public boolean insertImportDetail(int importId, int materialId, int quantity, double price) {
        String sql = """
            INSERT INTO ImportDetail (ImportId, MaterialId, Quantity, Price)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, importId);
            ps.setInt(2, materialId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
