package DAO;

import dal.DBContext;
import model.Users;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DBContext {

    // Get user by email & password (for login)
    public Users getUserByEmailAndPassword(String email, String password) {
        Users user = null;
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleId = r.RoleId WHERE u.Email = ? AND u.Password = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                    Role role = new Role();
                    role.setRoleId(user.getRoleId());
                    role.setRoleName(rs.getString("RoleName"));
                    user.setUserImage(rs.getString("UserImage")); // ✅ avatar từ DB

                    user.setRole(role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Get user by email
    public Users getUserByEmail(String email) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Get user by ID
    public Users getUserById(int id) {
        Users user = null;
        try {
            String sql = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new Users();
                user.setUserId(rs.getInt("userId"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setUserImage(rs.getString("userImage")); // Dòng này bắt buộc có!

                user.setRoleId(rs.getInt("roleId"));
                user.setIsActive(rs.getBoolean("isActive"));
                // thêm các trường khác nếu có
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // Get all users
    // Get all users (with RoleName attached)
    public List<Users> getAllUsers() {
        List<Users> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("userId"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("roleId"));
                user.setIsActive(rs.getBoolean("isActive"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Delete user
    public boolean deleteUserById(int userId) {
        String sql = "DELETE FROM Users WHERE UserId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all roles
    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM roles";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("roleId"));
                role.setRoleName(rs.getString("roleName"));
                list.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get role name
    public String getRoleName(int roleId) {
        String sql = "SELECT RoleName FROM Roles WHERE RoleId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("RoleName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    // Update password
    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update role and status
    public boolean updateUserRoleAndStatus(int userId, int roleId, boolean isActive) {
        try {
            String sql = "UPDATE users SET roleId = ?, isActive = ? WHERE userId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, roleId);
            ps.setBoolean(2, isActive);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Extract user from ResultSet
    private Users extractUserFromResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setUserId(rs.getInt("UserId"));
        user.setFullName(rs.getString("FullName"));
        user.setUserImage(rs.getString("UserImage"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setPassword(rs.getString("Password"));
        user.setRoleId(rs.getInt("RoleId"));
        user.setIsActive(rs.getBoolean("IsActive"));
        return user;
    }

    // Main for testing
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        Users u = dao.getUserByEmailAndPassword("giamdoc@example.com", "giamdoc123");
        System.out.println(u.getEmail());
    }
}
