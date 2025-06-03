package controller.general;

import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Role;
import model.Users;

@WebServlet(name = "UserDetailController", urlPatterns = {"/user-detail"})
public class UserDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        // Nếu id không có từ URL, lấy từ session
        if (idParam == null || idParam.isEmpty()) {
            idParam = (String) request.getSession().getAttribute("userId");
        }

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int userId = Integer.parseInt(idParam);
                UserDAO dao = new UserDAO();
                Users user = dao.getUserById(userId);
                List<Role> roleList = dao.getAllRoles();

                if (user != null) {
                    request.setAttribute("user", user);
                    request.setAttribute("roleList", roleList);
                    request.setAttribute("pageContent", "/UserDetail.jsp");
                    request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
                } else {
                    response.sendRedirect("userlist?message=user_not_found");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("userlist?message=invalid_id_format");
            }
        } else {
            response.sendRedirect("userlist?message=missing_id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
