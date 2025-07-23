/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.general;

import DAO.UserDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;
import model.Role;

/**
 *
 * @author Admin
 */
@WebServlet(name = "UserListController", urlPatterns = {"/userlist"})
public class UserListController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    UserDAO userDAO = new UserDAO();

    // Get search, filter, sort, and pagination parameters
    String searchQuery = request.getParameter("search");
    int roleId = request.getParameter("roleId") == null ? 0 : Integer.parseInt(request.getParameter("roleId"));
    int status = request.getParameter("status") == null ? -1 : Integer.parseInt(request.getParameter("status"));
    String sortBy = request.getParameter("sortBy") == null ? "userId" : request.getParameter("sortBy");
    String sortOrder = request.getParameter("sortOrder") == null ? "ASC" : request.getParameter("sortOrder");
    String pageStr = request.getParameter("page");
    int page = (pageStr == null) ? 1 : Integer.parseInt(pageStr);
    int pageSize = 10; // Number of users per page

    // Fetch paginated and filtered/sorted users and total count
    List<Users> userList = userDAO.getUsers(searchQuery, roleId, status, sortBy, sortOrder, page, pageSize);
    int totalUsers = userDAO.getUserCount(searchQuery, roleId, status);
    int totalPages = totalUsers > 0 ? (int) Math.ceil((double) totalUsers / pageSize) : 1;

    // Debug logging
    System.out.println("DEBUG UserList - totalUsers: " + totalUsers + ", pageSize: " + pageSize + ", totalPages: " + totalPages + ", currentPage: " + page);

    // Fetch all roles for the dropdowns/labels
    List<Role> roleList = userDAO.getAllRoles();

    // Set attributes for the JSP
    request.setAttribute("userList", userList);
    request.setAttribute("roleList", roleList);
    request.setAttribute("currentPage", page);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("totalUsers", totalUsers);
    request.setAttribute("pageSize", pageSize);
    request.setAttribute("searchQuery", searchQuery);
    request.setAttribute("roleId", roleId);
    request.setAttribute("status", status);
    request.setAttribute("sortBy", sortBy);
    request.setAttribute("sortOrder", sortOrder);
    request.setAttribute("pageContent", "/UserList.jsp");


    request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
}




/**
 * Handles the HTTP <code>POST</code> method.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
