/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class RoleFunction {
    private int roleId;
    private int functionId;
    private boolean isActive;
    
    public RoleFunction() {}
    
    public RoleFunction(int roleId, int functionId) {
        this.roleId = roleId;
        this.functionId = functionId;
        this.isActive = true; // default active
    }
    
    public RoleFunction(int roleId, int functionId, boolean isActive) {
        this.roleId = roleId;
        this.functionId = functionId;
        this.isActive = isActive;
    }
    
    public int getRoleId() {
        return roleId;
    }
    
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
    public int getFunctionId() {
        return functionId;
    }
    
    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
