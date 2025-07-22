package DAO;

import java.sql.*;
import java.util.*;
import dal.DBContext;

public class MaterialInventoryDAO {

    public Map<String, Integer> getInventoryByMaterialId(int materialId) throws SQLException {
        Map<String, Integer> inventory = new HashMap<>();

        String sql = "SELECT ms.StatusName, COALESCE(mi.Quantity, 0) AS Quantity "
                   + "FROM MaterialStatus ms "
                   + "LEFT JOIN MaterialInventory mi ON ms.StatusId = mi.StatusId AND mi.MaterialId = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String statusName = rs.getString("StatusName");
                    int quantity = rs.getInt("Quantity");
                    inventory.put(statusName, quantity);
                    System.out.println("Fetched inventory - Status: " + statusName + ", Quantity: " + quantity);
                }
            }
        }

        return inventory;
    }

    public void updateInventory(int materialId, int statusId, int quantity) throws SQLException {
        String sql = "INSERT INTO MaterialInventory (MaterialId, StatusId, Quantity) "
                   + "VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE Quantity = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Executing inventory update: MaterialId=" + materialId +
                               ", StatusId=" + statusId + ", Quantity=" + quantity);

            ps.setInt(1, materialId);
            ps.setInt(2, statusId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);

            int rows = ps.executeUpdate();
            System.out.println("Rows affected: " + rows);
        }
    }

    public void updateMaterialQuantity(int materialId, int totalQuantity) throws SQLException {
        String sql = "UPDATE Materials SET Quantity = ?, UpdatedAt = NOW() WHERE MaterialId = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("Updating total quantity in Materials: MaterialId=" + materialId + ", Quantity=" + totalQuantity);

            ps.setInt(1, totalQuantity);
            ps.setInt(2, materialId);

            int rows = ps.executeUpdate();
            System.out.println("Rows affected (Materials): " + rows);
        }
    }
}
