package DAO;

import dal.DBContext;
import model.Users;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DBContext {

    private final Connection conn;

    public UserDAO() {
        DBContext db = new DBContext();
        this.conn = db.getConnection();
    }

    // Get user by email & password (for login)
    public Users getUserByEmailAndPassword(String email, String password) {
        Users user = null;
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleId = r.RoleId WHERE u.Email = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("Password");
                    boolean isMatch = false;

                    // Nếu trùng trực tiếp (password gốc)
                    if (password.equals(dbPassword)) {
                        isMatch = true;
                    } else {
                        // Nếu là mật khẩu băm, so sánh với mật khẩu sau khi băm
                        String hashedInput = utils.HashUtil.hashPassword(password);
                        if (hashedInput.equals(dbPassword)) {
                            isMatch = true;
                        }
                    }

                    if (isMatch) {
                        user = extractUserFromResultSet(rs);
                        Role role = new Role();
                        role.setRoleId(user.getRoleId());
                        role.setRoleName(rs.getString("RoleName"));
                        user.setUserImage(rs.getString("UserImage"));
                        user.setRole(role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Users getUserByEmail(String email) {
        Users user = null;
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleID = r.RoleID WHERE u.Email = ?";
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
                user.setPhone(rs.getString("phone"));

                user.setUserImage(rs.getString("userImage"));
                user.setRoleId(rs.getInt("roleId"));
                user.setIsActive(rs.getBoolean("isActive"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

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

    public boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE Email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, email.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi update mật khẩu cho email: " + email);
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
        user.setUserId(rs.getInt("UserID"));
        user.setFullName(rs.getString("FullName"));
        user.setUserImage(rs.getString("UserImage"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setPassword(rs.getString("Password"));
        user.setRoleId(rs.getInt("RoleID"));
        user.setIsActive(rs.getBoolean("IsActive"));

        // Gán role
        Role role = new Role();
        role.setRoleId(rs.getInt("RoleID")); // lấy từ bảng Users
        role.setRoleName(rs.getString("RoleName")); // lấy từ bảng Roles
        user.setRole(role);

        return user;
    }

    public Users getUserByEmailAndPhone(String email, String phone) {
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleId = r.RoleId WHERE u.Email = ? AND u.Phone = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertPasswordResetRequest(int userId) {
        String sql = "INSERT INTO PasswordResetRequest (UserId) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        Users u = dao.getUserByEmailAndPassword("giamdoc@example.com", "giamdoc123");
        if (u != null) {
            System.out.println("Đăng nhập thành công: " + u.getEmail());
        } else {
            System.out.println("Sai email hoặc mật khẩu");
        }

        System.out.println(u.getEmail());
    }

}
