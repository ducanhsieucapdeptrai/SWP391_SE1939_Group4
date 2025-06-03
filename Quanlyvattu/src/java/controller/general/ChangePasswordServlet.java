
package controller.general;

import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Users;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (email == null || oldPassword == null || newPassword == null || confirmPassword == null ||
                email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        UserDAO dao = new UserDAO();
        Users user = dao.getUserByEmail(email);
        if (user == null) {
            request.setAttribute("error", "Email không tồn tại.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        if (!oldPassword.equals(user.getPassword())) {
            request.setAttribute("error", "Mật khẩu cũ không đúng.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        boolean success = dao.updatePassword(user.getUserId(), newPassword);
        if (success) {
            request.setAttribute("message", "Đổi mật khẩu thành công!");
        } else {
            request.setAttribute("error", "Đã xảy ra lỗi khi cập nhật mật khẩu.");
        }
        request.getRequestDispatcher("change_password.jsp").forward(request, response);
    }
}