/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */

import java.sql.Timestamp;

public class Users {
    private int userId;
    private String fullName;
    private String UserImage;
    private String email;
    private String phone;
    private String password;
    private int roleId;
    private boolean isActive;
    private Role role;
//    private Timestamp createdAt;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Constructors
    public Users() {
    }

    
//
//    public Timestamp getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Timestamp createdAt) {
//        this.createdAt = createdAt;
//    }

    public Users(int userId, String fullName, String UserImage, String email, String phone, String password, int roleId, boolean isActive, Role role) {
        this.userId = userId;
        this.fullName = fullName;
        this.UserImage = UserImage;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roleId = roleId;
        this.isActive = isActive;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String UserImage) {
        this.UserImage = UserImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    
}
