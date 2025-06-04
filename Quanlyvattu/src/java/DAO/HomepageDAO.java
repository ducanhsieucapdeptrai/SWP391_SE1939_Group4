package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class for retrieving homepage statistics
 */
public class HomepageDAO {

    /**
     * Get total number of materials in the system
     *
     * @return total number of materials
     */
    public int getTotalMaterialCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Materials";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting total material count: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Get total number of imports
     *
     * @return total number of imports
     */
    public int getTotalImportCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM ImportList";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting total import count: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Get total number of exports
     *
     * @return total number of exports
     */
    public int getTotalExportCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM ExportList";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting total export count: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Get total number of pending requests
     *
     * @return total number of pending requests
     */
    public int getPendingRequestCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM RequestList WHERE Status = 'Pending'";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting pending request count: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Get today's sales count
     *
     * @return today's sales count
     */
}
