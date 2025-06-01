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
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Role(rs.getInt(1), rs.getString(2)));
            }
        }
        return list;
    }
    
    public List<Modules> getAllModules() throws Exception {
        List<Modules> list = new ArrayList<>();
        String sql = "SELECT * FROM Modules";
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Modules(rs.getInt(1), rs.getString(2)));
            }
        }
        return list;
    }
    
    public List<Functions> getAllFunctions() throws Exception {
        List<Functions> list = new ArrayList<>();
        String sql = "SELECT * FROM Functions";
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Functions(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        }
        return list;
    }
    
    public List<Functions> getFunctionsByModule(int moduleId) throws Exception {
        List<Functions> list = new ArrayList<>();
        String sql = "SELECT * FROM Functions WHERE ModuleId = ?";
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
        String sql = "SELECT RoleId, FunctionId FROM FunctionDetails";
        try (Connection conn = new DBContext().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                set.add(rs.getInt("RoleId") + ":" + rs.getInt("FunctionId"));
            }
        }
        return set;
    }
    
    public void updatePermissions(Set<String> newPairs) throws Exception {
        String deleteSQL = "DELETE FROM FunctionDetails";
        String insertSQL = "INSERT INTO FunctionDetails (RoleId, FunctionId) VALUES (?, ?)";
        
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Delete all existing permissions
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(deleteSQL);
                }
                
                // Insert new permissions
                if (!newPairs.isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                        for (String pair : newPairs) {
                            String[] parts = pair.split(":");
                            if (parts.length == 2) {
                                ps.setInt(1, Integer.parseInt(parts[0]));
                                ps.setInt(2, Integer.parseInt(parts[1]));
                                ps.addBatch();
                            }
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
}