package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportDetailDAO {

    // Thêm 1 dòng chi tiết nhập kho
    public boolean insertImportDetail(int importId, int materialId, int quantity, double price) {
        String sql = """
            INSERT INTO ImportDetail (ImportId, MaterialId, Quantity, Price)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, importId);
            ps.setInt(2, materialId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting import detail: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
