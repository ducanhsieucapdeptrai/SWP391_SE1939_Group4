/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.general;

import DAO.RoleFunctionDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.*;

@WebServlet(name = "UserAuthorizationMatrixController", urlPatterns = {"/user-matrix"})
public class UserAuthorizationMatrixController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            RoleFunctionDAO dao = new RoleFunctionDAO();

            // Load all data
            List<Role> roles = dao.getAllRoles();
            List<Modules> modules = dao.getAllModules();
            List<Functions> allFunctions = dao.getAllFunctions();
            Set<String> roleFunctionPairs = dao.getAllRoleFunctionPairs();

            // Handle role selection and filtering
            String selectedRoleId = request.getParameter("selectedRole");
            String selectedStatus = request.getParameter("selectedStatus");
            
            // Set default status if not provided
            if (selectedStatus == null || selectedStatus.isEmpty()) {
                selectedStatus = "all";
            }

            // Get selected role name for display
            String selectedRoleName = "";
            if (selectedRoleId != null && !selectedRoleId.isEmpty()) {
                for (Role role : roles) {
                    if (String.valueOf(role.getRoleId()).equals(selectedRoleId)) {
                        selectedRoleName = role.getRoleName();
                        break;
                    }
                }
            }

            // Set attributes
            request.setAttribute("roles", roles);
            request.setAttribute("modules", modules);
            request.setAttribute("functions", allFunctions);
            request.setAttribute("roleFunctionPairs", roleFunctionPairs);
            request.setAttribute("selectedRoleName", selectedRoleName);
            
            // Set parameters as attributes for JSP access
            request.setAttribute("selectedRole", selectedRoleId);
            request.setAttribute("selectedStatus", selectedStatus);
            
            request.setAttribute("pageContent", "/userauthorization.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String[] values = request.getParameterValues("perm");
            String selectedRoleId = request.getParameter("selectedRole");
            String selectedStatus = request.getParameter("selectedStatus");
            
            if (selectedRoleId == null || selectedRoleId.isEmpty()) {
                throw new Exception("No role selected for update");
            }
            
            RoleFunctionDAO dao = new RoleFunctionDAO();
            int roleId = Integer.parseInt(selectedRoleId);
            
            // Get functions that are currently visible (based on filter)
            List<Functions> visibleFunctions = getVisibleFunctionsForRole(dao, roleId, selectedStatus);
            Set<Integer> visibleFunctionIds = new HashSet<>();
            for (Functions func : visibleFunctions) {
                visibleFunctionIds.add(func.getFunctionId());
            }
            
            // Get selected function IDs from form
            Set<Integer> selectedFunctionIds = new HashSet<>();
            if (values != null) {
                for (String value : values) {
                    String[] parts = value.split(":");
                    if (parts.length == 2 && parts[0].equals(selectedRoleId)) {
                        selectedFunctionIds.add(Integer.parseInt(parts[1]));
                    }
                }
            }
            
            // Update ONLY visible functions
            dao.updateRolePermissionsSelective(roleId, visibleFunctionIds, selectedFunctionIds);
            
            // Protect critical admin functions - ensure admin always has access
            if (roleId == 1) { // Warehouse Manager
                dao.ensureCriticalAdminPermissions(roleId);
            }
            
            HttpSession session = request.getSession();
            session.setAttribute("message", "Permissions updated successfully for the selected role!");
            session.setAttribute("messageType", "success");
            
            // Redirect back with same parameters
            String redirectUrl = "user-matrix";
            redirectUrl += "?selectedRole=" + selectedRoleId;
            if (selectedStatus != null && !selectedStatus.isEmpty()) {
                redirectUrl += "&selectedStatus=" + selectedStatus;
            }
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("message", "Error saving permissions: " + e.getMessage());
            session.setAttribute("messageType", "error");
            response.sendRedirect("user-matrix");
        }
    }
    
    private List<Functions> getVisibleFunctions(RoleFunctionDAO dao, String selectedStatus) throws Exception {
        List<Functions> allFunctions = dao.getAllFunctions();
        if (selectedStatus == null || selectedStatus.equals("all")) {
            return allFunctions;
        }
        
        // For filtered status, we need to return all functions because
        // the filtering is done in JSP based on current permissions
        // We cannot filter here without knowing the selected role
        return allFunctions;
    }
    
    private List<Functions> getVisibleFunctionsForRole(RoleFunctionDAO dao, int roleId, String selectedStatus) throws Exception {
        List<Functions> allFunctions = dao.getAllFunctions();
        
        if (selectedStatus == null || selectedStatus.equals("all")) {
            return allFunctions;
        }
        
        // Get current permissions for this role
        Set<String> roleFunctionPairs = dao.getAllRoleFunctionPairs();
        List<Functions> filteredFunctions = new ArrayList<>();
        
        for (Functions func : allFunctions) {
            String pairKey = roleId + ":" + func.getFunctionId();
            boolean hasPermission = roleFunctionPairs.contains(pairKey);
            
            if ((selectedStatus.equals("active") && hasPermission) ||
                (selectedStatus.equals("inactive") && !hasPermission)) {
                filteredFunctions.add(func);
            }
        }
        
        return filteredFunctions;
    }
}
