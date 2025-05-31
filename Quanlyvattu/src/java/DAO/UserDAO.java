package DAO;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Users;

/**
 *
 * @author Admin
 */
public class UserDAO extends DBContext {
    public Users getUserById(String id) {
        String sql = "SELECT UserId, FullName, Gender, Phone, Email, RoleId, IsActive FROM Users WHERE UserId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("UserId"));
                user.setFullName(rs.getString("FullName"));
                user.setPhone(rs.getString("Phone"));
                user.setEmail(rs.getString("Email"));
                user.setRoleId(rs.getInt("RoleId"));
                user.setIsActive(rs.getBoolean("IsActive"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
