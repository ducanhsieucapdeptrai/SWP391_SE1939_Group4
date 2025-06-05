package DAO;

import dal.DBContext;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO extends DBContext {

    // Lấy danh sách tất cả các role
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Roles";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    // Lấy role theo ID
    public Role getRoleById(int roleId) {
        String sql = "SELECT * FROM Roles WHERE RoleId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("RoleId"), rs.getString("RoleName"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm role mới
    public boolean insertRole(Role role) {
        String sql = "INSERT INTO Roles (RoleId, RoleName) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, role.getRoleId());
            ps.setString(2, role.getRoleName());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật role
    public boolean updateRole(Role role) {
        String sql = "UPDATE Roles SET RoleName = ? WHERE RoleId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.getRoleName());
            ps.setInt(2, role.getRoleId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa role
    public boolean deleteRole(int roleId) {
        String sql = "DELETE FROM Roles WHERE RoleId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
