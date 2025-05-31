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

@WebServlet(name="UserAuthorizationMatrixController", urlPatterns={"/user-matrix"})
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
            
            // Set attributes
            request.setAttribute("roles", roles);
            request.setAttribute("modules", modules);
            request.setAttribute("functions", allFunctions);
            request.setAttribute("roleFunctionPairs", roleFunctionPairs);
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
            Set<String> newPairs = new HashSet<>();
            
            if (values != null) {
                newPairs.addAll(Arrays.asList(values));
            }
            
           
            newPairs.add("1:34"); 
            
            new RoleFunctionDAO().updatePermissions(newPairs);
            response.sendRedirect("user-matrix");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}