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
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleId = r.RoleId WHERE u.Email = ?";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("Password");
                    boolean isMatch = false;

                    if (password.equals(dbPassword)) {
                        isMatch = true;
                    } else {
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
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleID = r.RoleID WHERE u.Email = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Users getUserById(int id) {
        String sql = "SELECT * FROM Users WHERE UserId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users();
                    user.setUserId(rs.getInt("UserId"));
                    user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhone(rs.getString("Phone"));
                    user.setUserImage(rs.getString("UserImage"));
                    user.setRoleId(rs.getInt("RoleId"));
                    user.setIsActive(rs.getBoolean("IsActive"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Users> getUsers(String searchQuery, int roleId, int status, String sortBy, String sortOrder, int page, int pageSize) {
        List<Users> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Users WHERE 1=1");

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append(" AND (FullName LIKE ? OR Email LIKE ?)");
        }
        if (roleId > 0) {
            sql.append(" AND RoleId = ?");
        }
        if (status != -1) {
            sql.append(" AND IsActive = ?");
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ").append(sortBy).append(" ").append(sortOrder);
        } else {
            sql.append(" ORDER BY userId ASC");
        }

        sql.append(" LIMIT ? OFFSET ?");

        // Debug logging
        System.out.println("DEBUG getUsers - SQL: " + sql.toString());
        System.out.println("DEBUG getUsers - searchQuery: " + searchQuery + ", roleId: " + roleId + ", status: " + status + ", page: " + page + ", pageSize: " + pageSize);

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchQuery + "%");
                ps.setString(paramIndex++, "%" + searchQuery + "%");
            }
            if (roleId > 0) {
                ps.setInt(paramIndex++, roleId);
            }
            if (status != -1) {
                ps.setInt(paramIndex++, status);
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setUserId(rs.getInt("UserId"));
                    user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setRoleId(rs.getInt("RoleId"));
                    user.setIsActive(rs.getBoolean("IsActive"));
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getUserCount(String searchQuery, int roleId, int status) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Users WHERE 1=1");

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append(" AND (FullName LIKE ? OR Email LIKE ?)");
        }
        if (roleId > 0) {
            sql.append(" AND RoleId = ?");
        }
        if (status != -1) {
            sql.append(" AND IsActive = ?");
        }

        // Debug logging
        System.out.println("DEBUG getUserCount - SQL: " + sql.toString());
        System.out.println("DEBUG getUserCount - searchQuery: " + searchQuery + ", roleId: " + roleId + ", status: " + status);

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchQuery + "%");
                ps.setString(paramIndex++, "%" + searchQuery + "%");
            }
            if (roleId > 0) {
                ps.setInt(paramIndex++, roleId);
            }
            if (status != -1) {
                ps.setInt(paramIndex++, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                    System.out.println("DEBUG getUserCount - count result: " + count);
                }
            }
        } catch (SQLException e) {
            System.out.println("DEBUG getUserCount - SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public List<Users> getAllUsers() {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("UserId"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setRoleId(rs.getInt("RoleId"));
                user.setIsActive(rs.getBoolean("IsActive"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteUserById(int userId) {
        String sql = "DELETE FROM Users WHERE UserId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Roles";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("RoleId"));
                role.setRoleName(rs.getString("RoleName"));
                list.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getRoleName(int roleId) {
        String sql = "SELECT RoleName FROM Roles WHERE RoleId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("RoleName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    public boolean updatePassword(String email, String hashedPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE Email = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, email.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi update mật khẩu cho email: " + email);
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserRoleAndStatus(int userId, int roleId, boolean isActive) {
        String sql = "UPDATE Users SET RoleId = ?, IsActive = ? WHERE UserId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ps.setBoolean(2, isActive);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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

        Role role = new Role();
        role.setRoleId(rs.getInt("RoleId"));
        role.setRoleName(rs.getString("RoleName"));
        user.setRole(role);

        return user;
    }

    public Users getUserByEmailAndPhone(String email, String phone) {
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleId = r.RoleId WHERE u.Email = ? AND u.Phone = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertPasswordResetRequest(int userId) {
        String sql = "INSERT INTO PasswordResetRequest (UserId) VALUES (?)";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Users getUserByPhone(String phone) {
        String sql = "SELECT * FROM Users WHERE Phone = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUser(Users user) {
        String sql = "INSERT INTO Users (FullName, UserImage, Email, Phone, Password, RoleId, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUserImage());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getPassword());
            ps.setInt(6, user.getRoleId());
            ps.setBoolean(7, user.isIsActive());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    public List<Users> getUsersByRoleNames(List<String> roleNames) {
        List<Users> list = new ArrayList<>();
        if (roleNames == null || roleNames.isEmpty()) {
            return list;
        }

        String placeholders = String.join(",", java.util.Collections.nCopies(roleNames.size(), "?"));
        String sql = "SELECT u.*, r.RoleName FROM Users u JOIN Roles r ON u.RoleId = r.RoleId WHERE r.RoleName IN (" + placeholders + ")";

        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < roleNames.size(); i++) {
                ps.setString(i + 1, roleNames.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users user = extractUserFromResultSet(rs);
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        Users u = dao.getUserByEmailAndPassword("giamdoc@example.com", "giamdoc123");
        if (u != null) {
            System.out.println("Đăng nhập thành công: " + u.getEmail());
        } else {
            System.out.println("Sai email hoặc mật khẩu");
        }
    }
}
