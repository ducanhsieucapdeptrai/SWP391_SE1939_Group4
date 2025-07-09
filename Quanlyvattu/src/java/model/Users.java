package model;

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

    // Constructors
    public Users() {}

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

    // Getters and Setters
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // ✅ New: Helper to get role name safely
    public String getRoleName() {
        return role != null ? role.getRoleName() : null;
    }

    // ✅ New: Helper to check if this user is Warehouse Staff
    public boolean isWarehouseStaff() {
        return "Warehouse Staff".equalsIgnoreCase(getRoleName());
    }
}
