/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.*;
import model.*;
import dal.DBContext;

public class RoleFunctionDAO extends DBContext {

    public List<Role> getAllRoles() throws Exception {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Roles";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Role(rs.getInt(1), rs.getString(2)));
            }
        }
        return list;
    }

    public List<Modules> getAllModules() throws Exception {
        List<Modules> list = new ArrayList<>();
        String sql = "SELECT * FROM Modules";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Modules(rs.getInt(1), rs.getString(2)));
            }
        }
        return list;
    }

    public List<Functions> getAllFunctions() throws Exception {
        List<Functions> list = new ArrayList<>();
        String sql = "SELECT FunctionId, FunctionName, Url, ModuleId FROM Functions";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Functions(rs.getInt("FunctionId"), rs.getString("FunctionName"),
                        rs.getString("Url"), rs.getInt("ModuleId")));
            }
        }
        return list;
    }

    public List<Functions> getFunctionsByModule(int moduleId) throws Exception {
        List<Functions> list = new ArrayList<>();
        String sql = "SELECT * FROM Functions WHERE ModuleId = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, moduleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Functions(rs.getInt(1), rs.getString(2), rs.getInt(3)));
                }
            }
        }
        return list;
    }

    public Set<String> getAllRoleFunctionPairs() throws Exception {
        Set<String> set = new HashSet<>();
        String sql = "SELECT RoleId, FunctionId FROM RoleFunction WHERE IsActive = 1";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                set.add(rs.getInt("RoleId") + ":" + rs.getInt("FunctionId"));
            }
        }
        return set;
    }

    public void updatePermissions(Set<String> newPairs) throws Exception {
        String updateInactiveSQL = "UPDATE RoleFunction SET IsActive = 0";
        String updateActiveSQL = "UPDATE RoleFunction SET IsActive = 1 WHERE RoleId = ? AND FunctionId = ?";
        String insertSQL = "INSERT INTO RoleFunction (RoleId, FunctionId, IsActive) VALUES (?, ?, 1)";

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Set all existing permissions to inactive (IsActive = 0)
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(updateInactiveSQL);
                }

                // Activate selected permissions (IsActive = 1)
                if (!newPairs.isEmpty()) {
                    try (PreparedStatement updatePs = conn.prepareStatement(updateActiveSQL); PreparedStatement insertPs = conn.prepareStatement(insertSQL)) {

                        for (String pair : newPairs) {
                            String[] parts = pair.split(":");
                            if (parts.length == 2) {
                                int roleId = Integer.parseInt(parts[0]);
                                int functionId = Integer.parseInt(parts[1]);

                                // Try to update existing record first
                                updatePs.setInt(1, roleId);
                                updatePs.setInt(2, functionId);
                                int rowsUpdated = updatePs.executeUpdate();

                                // If no rows updated, insert new record
                                if (rowsUpdated == 0) {
                                    insertPs.setInt(1, roleId);
                                    insertPs.setInt(2, functionId);
                                    insertPs.addBatch();
                                }
                            }
                        }
                        insertPs.executeBatch();
                    }
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // NEW METHOD: Update permissions for specific role only affecting visible functions
    public void updateRolePermissionsSelective(int roleId, Set<Integer> visibleFunctionIds, Set<Integer> selectedFunctionIds) throws Exception {
        String updateSQL = "UPDATE RoleFunction SET IsActive = ? WHERE RoleId = ? AND FunctionId = ?";
        String insertSQL = "INSERT INTO RoleFunction (RoleId, FunctionId, IsActive) VALUES (?, ?, ?)";

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement updatePs = conn.prepareStatement(updateSQL); PreparedStatement insertPs = conn.prepareStatement(insertSQL)) {

                    // Update only visible functions
                    for (Integer functionId : visibleFunctionIds) {
                        int isActive = selectedFunctionIds.contains(functionId) ? 1 : 0;

                        // Try to update existing record first
                        updatePs.setInt(1, isActive);
                        updatePs.setInt(2, roleId);
                        updatePs.setInt(3, functionId);
                        int rowsUpdated = updatePs.executeUpdate();

                        // If no rows updated, insert new record
                        if (rowsUpdated == 0) {
                            insertPs.setInt(1, roleId);
                            insertPs.setInt(2, functionId);
                            insertPs.setInt(3, isActive);
                            insertPs.addBatch();
                        }
                    }
                    insertPs.executeBatch();
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void ensureCriticalAdminPermissions(int roleId) throws Exception {
        String updateSQL = "UPDATE RoleFunction SET IsActive = 1 WHERE RoleId = ? AND FunctionId = ?";
        String insertSQL = "INSERT INTO RoleFunction (RoleId, FunctionId, IsActive) VALUES (?, ?, 1)";
        int[] criticalFunctionIds = {1, 2, 3};

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                for (int functionId : criticalFunctionIds) {
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSQL)) {
                        updatePs.setInt(1, roleId);
                        updatePs.setInt(2, functionId);
                        int updated = updatePs.executeUpdate();

                        if (updated == 0) {
                            try (PreparedStatement insertPs = conn.prepareStatement(insertSQL)) {
                                insertPs.setInt(1, roleId);
                                insertPs.setInt(2, functionId);
                                insertPs.executeUpdate();
                            }
                        }
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public boolean hasPermission(int roleId, String functionUrl) throws Exception {
        String sql = "SELECT COUNT(*) FROM RoleFunction rf "
                + "JOIN Functions f ON rf.FunctionId = f.FunctionId "
                + "WHERE rf.RoleId = ? AND f.Url = ? AND rf.IsActive = 1";

        try (Connection conn = new DBContext().getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ps.setString(2, functionUrl);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Set<Integer> getFunctionsByRole(int roleId) throws Exception {
        Set<Integer> functionIds = new HashSet<>();
        String sql = "SELECT FunctionId FROM RoleFunction WHERE RoleId = ? AND IsActive = 1";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    functionIds.add(rs.getInt("FunctionId"));
                }
            }
        }
        return functionIds;
    }

    public Set<String> getRoleFunctionPairsFiltered(Integer roleId, Boolean isActive) throws Exception {
        Set<String> set = new HashSet<>();
        StringBuilder sql = new StringBuilder("SELECT RoleId, FunctionId FROM RoleFunction WHERE 1=1");

        if (roleId != null) {
            sql.append(" AND RoleId = ?");
        }
        if (isActive != null) {
            sql.append(" AND IsActive = ?");
        }

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (roleId != null) {
                ps.setInt(paramIndex++, roleId);
            }
            if (isActive != null) {
                ps.setInt(paramIndex, isActive ? 1 : 0);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    set.add(rs.getInt("RoleId") + ":" + rs.getInt("FunctionId"));
                }
            }
        }
        return set;
    }

    public void updateRolePermissions(int roleId, Set<Integer> functionIds) throws Exception {
        String deleteSQL = "DELETE FROM RoleFunction WHERE RoleId = ?";
        String insertSQL = "INSERT INTO RoleFunction (RoleId, FunctionId, IsActive) VALUES (?, ?, 1)";

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Delete existing permissions for this role only
                try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                    ps.setInt(1, roleId);
                    ps.executeUpdate();
                }

                // Insert new permissions for this role
                if (!functionIds.isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                        for (Integer functionId : functionIds) {
                            ps.setInt(1, roleId);
                            ps.setInt(2, functionId);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Functions> getAllFunctionsWithModules() throws Exception {
        List<Functions> list = new ArrayList<>();
        String sql = "SELECT f.FunctionId, f.FunctionName, f.Url, f.ModuleId, m.ModuleName "
                + "FROM Functions f JOIN Modules m ON f.ModuleId = m.ModuleId "
                + "ORDER BY m.ModuleName, f.FunctionName";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Functions func = new Functions(
                        rs.getInt("FunctionId"),
                        rs.getString("FunctionName"),
                        rs.getString("Url"),
                        rs.getInt("ModuleId")
                );
                list.add(func);
            }
        }
        return list;
    }
}
