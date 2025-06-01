/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class FunctionDetail {
    private int roleId;
    private int functionId;
    
    public FunctionDetail() {}
    
    public FunctionDetail(int roleId, int functionId) {
        this.roleId = roleId;
        this.functionId = functionId;
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
}