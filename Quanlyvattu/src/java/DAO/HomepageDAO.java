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
        String sql = "SELECT COUNT(*) FROM Materials";

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting total material count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total number of imports
     *
     * @return total number of imports
     */
    public int getTotalImportCount() {
        String sql = "SELECT COUNT(*) FROM ImportList";

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting total import count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total number of exports
     *
     * @return total number of exports
     */
    public int getTotalExportCount() {
        String sql = "SELECT COUNT(*) FROM ExportList";

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting total export count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total number of pending requests
     *
     * @return total number of pending requests
     */
    public int getPendingRequestCount() {
        String sql = "SELECT COUNT(*) FROM RequestList WHERE Status = 'Pending'";

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting pending request count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get today's sales count (placeholder for future logic)
     *
     * @return today's sales count
     */
    public int getTodaySalesCount() {
        // TODO: Implement logic based on your business rule
        return 0;
    }
}
