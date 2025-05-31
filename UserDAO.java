package DAO;

import dal.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Users;
import model.Role;

/**
 *
 * @author Admin
 */
public class UserDAO extends DBContext {
    public Users getUserById(String id) {
        String sql = "SELECT u.UserId, u.FullName, u.Email, u.Phone, u.Password, u.RoleId, u.IsActive FROM Users u " +
                    "JOIN Roles r ON u.RoleId = r.RoleId " +
                    "WHERE u.UserId = ?";
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
                user.setPassword(rs.getString("Password"));
                user.setRoleId(rs.getInt("RoleId"));
                
                user.setIsActive(rs.getBoolean("IsActive"));
                
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    
    public List<Users> getAllUsers() {
        List<Users> userList = new ArrayList<>();
        String sql = "SELECT u.UserId, u.FullName, u.Email, u.Phone,r.RoleId, u.IsActive FROM Users u " +
                    "JOIN Roles r ON u.RoleId = r.RoleId";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("UserId"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setRoleId(rs.getInt("RoleId"));
                
                user.setIsActive(rs.getBoolean("IsActive"));
                
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return userList;
    }
    
    public boolean deleteUserById(int userId) {
    String sql = "DELETE FROM Users WHERE UserId = ?";
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setInt(1, userId);
        int rows = st.executeUpdate();
        return rows > 0;
    } catch (SQLException e) {
        System.err.println("Error deleting user by ID: " + e.getMessage());
        return false;
    }
}

    
    public List<Role> getAllRoles() {
        List<Role> roleList = new ArrayList<>();
        String sql = "SELECT RoleId, RoleName FROM Roles";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("RoleId"));
                role.setRoleName(rs.getString("RoleName"));
                roleList.add(role);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return roleList;
    }
    
    public String getRoleName(int roleId) {
        String sql = "SELECT RoleName FROM Roles WHERE RoleId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("RoleName");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "Unknown";
    }
    
    public boolean updateUserRoleAndStatus(int userId, int roleId, boolean isActive) {
    String sql = "UPDATE Users SET RoleId = ?, IsActive = ? WHERE UserId = ?";
    
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setInt(1, roleId);
        st.setBoolean(2, isActive);
        st.setInt(3, userId);
        
        int rowsAffected = st.executeUpdate();
        return rowsAffected > 0;
        
    } catch (SQLException e) {
        System.err.println("Error updating user role/status: " + e.getMessage());
        return false;
    }
    
    }
    public Users getUserByEmail(String email) {
    String sql = "SELECT UserId, FullName, Email, Phone, Password, RoleId, IsActive FROM Users WHERE Email = ?";
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setString(1, email);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Users user = new Users();
            user.setUserId(rs.getInt("UserId"));
            user.setFullName(rs.getString("FullName"));
            user.setEmail(rs.getString("Email"));
            user.setPhone(rs.getString("Phone"));
            user.setPassword(rs.getString("Password"));
            user.setRoleId(rs.getInt("RoleId"));
            user.setIsActive(rs.getBoolean("IsActive"));
            return user;
        }
    } catch (SQLException e) {
        System.out.println("Error in getUserByEmail: " + e.getMessage());
    }
    return null;
}
public boolean updatePassword(int userId, String newHashedPassword) {
    String sql = "UPDATE Users SET Password = ? WHERE UserId = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, newHashedPassword);
        ps.setInt(2, userId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}

