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
import model.Role;
import model.Users;

@WebServlet(name = "UserEditRole_IsActiveController", urlPatterns = {"/user-edit"})
public class UserEditRole_StatusController extends HttpServlet {

    private UserDAO dao = new UserDAO();

    // Hiển thị form chỉnh sửa
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect("userlist?message=missing_id");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Users user = dao.getUserById(id); // Sử dụng phương thức getUserById(int id)

            if (user == null) {
                response.sendRedirect("userlist?message=invalid_id");
                return;
            }

            List<Role> roles = dao.getAllRoles();

            request.setAttribute("user", user);
            request.setAttribute("roles", roles);
            request.setAttribute("pageContent", "/UserEditRole_Status.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("userlist?message=invalid_id");
        }
    }

    // Xử lý khi submit form chỉnh sửa
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

            boolean success = dao.updateUserRoleAndStatus(userId, roleId, isActive);
            if (success) {
                response.sendRedirect("user-detail?id=" + userId + "&message=update_success");
            } else {
                response.sendRedirect("user-detail?id=" + userId + "&message=update_error");
            }
        } catch (Exception e) {
            response.sendRedirect("userlist?message=update_error");
        }
    }
}
