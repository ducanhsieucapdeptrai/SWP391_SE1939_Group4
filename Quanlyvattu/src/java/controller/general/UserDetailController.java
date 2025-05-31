/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.general;

import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Users;

public class UserDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        
        if (id != null && !id.isEmpty()) {
            UserDAO dao = new UserDAO();
            Users user = dao.getUserById(id);
            
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("UserDetail.jsp").forward(request, response);
            } else {
                response.sendRedirect("homepage.jsp");
            }
        } else {
            response.sendRedirect("homepage.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
