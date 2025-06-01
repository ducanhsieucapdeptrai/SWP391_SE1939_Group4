package controller.general;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import DAO.UserDAO;
import model.Users;
import utils.HashUtil;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy email và các trường từ form
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Kiểm tra các trường không được bỏ trống
        if (email == null || currentPassword == null || newPassword == null || confirmPassword == null
                || email.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Please fill in all fields.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        Users user = userDAO.getUserByEmail(email); // cần viết hàm này trong DAO

        if (user == null) {
            request.setAttribute("error", "Email not found.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        String hashedCurrentPassword = HashUtil.hashPassword(currentPassword);
        if (!hashedCurrentPassword.equals(user.getPassword())) {
            request.setAttribute("error", "Current password is incorrect.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        String hashedNewPassword = HashUtil.hashPassword(newPassword);
        boolean updated = userDAO.updatePassword(user.getUserId(), hashedNewPassword);

        if (updated) {
            user.setPassword(hashedNewPassword);
            request.setAttribute("message", "Password changed successfully.");
        } else {
            request.setAttribute("error", "Failed to update password.");
        }

        request.getRequestDispatcher("change_password.jsp").forward(request, response);
    }
}