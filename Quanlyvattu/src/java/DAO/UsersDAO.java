package DAO;

import dal.DBContext;
import model.Users;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    public static Users getUserByEmailAndPassword(String email, String password) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

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

    public static Users getUserByEmail(String email) {
        Users user = null;
        String sql = "SELECT * FROM Users WHERE Email = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public static List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Roles";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("RoleId"));
                role.setRoleName(rs.getString("RoleName"));
                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public static boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE Email = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static Users extractUserFromResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setUserId(rs.getInt("UserId"));
        user.setFullName(rs.getString("FullName"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setPassword(rs.getString("Password"));
        user.setRoleId(rs.getInt("RoleId"));
        user.setIsActive(rs.getBoolean("IsActive"));
//        user.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return user;
    }

    public static void main(String[] args) {
        UsersDAO dao = new UsersDAO();

        Users u = dao.getUserByEmailAndPassword("giamdoc@example.com", "giamdoc123");
        System.out.println(u.getEmail());

    }
}
