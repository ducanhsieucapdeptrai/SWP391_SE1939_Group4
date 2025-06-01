/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Functions {
    private int functionId;
    private String functionName;
    private int moduleId;
    
    public Functions() {}
    
    public Functions(int functionId, String functionName, int moduleId) {
        this.functionId = functionId;
        this.functionName = functionName;
        this.moduleId = moduleId;
    }
    
    public int getFunctionId() {
        return functionId;
    }
    
    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }
    
    public String getFunctionName() {
        return functionName;
    }
    
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    
    public int getModuleId() {
        return moduleId;
    }
    
    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }
}
