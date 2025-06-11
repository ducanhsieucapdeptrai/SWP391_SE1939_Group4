package controller.general;

import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Users;
import utils.HashUtil;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email").trim();
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        System.out.println(">>> Email nhập: " + email);

        if (email == null || oldPassword == null || newPassword == null || confirmPassword == null
                || email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
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

        // So sánh mật khẩu cũ (có thể là hash hoặc plain text trong DB)
        String storedPassword = user.getPassword();
        boolean isOldPasswordCorrect = false;

        if (storedPassword.length() == 64 && storedPassword.matches("[0-9a-fA-F]+")) {
            String hashedOldPassword = HashUtil.hashPassword(oldPassword);
            if (hashedOldPassword.equals(storedPassword)) {
                isOldPasswordCorrect = true;
            }
        } else {
            if (oldPassword.equals(storedPassword)) {
                isOldPasswordCorrect = true;
            }
        }

        if (!isOldPasswordCorrect) {
            request.setAttribute("error", "Mật khẩu cũ không đúng.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            request.getRequestDispatcher("change_password.jsp").forward(request, response);
            return;
        }

        // Băm mật khẩu mới và cập nhật vào DB
        String hashedNewPassword = HashUtil.hashPassword(newPassword);
        boolean success = dao.updatePassword(email, hashedNewPassword);
     
        if (success) {
            request.setAttribute("message", "Đổi mật khẩu thành công!");
        } else {
            request.setAttribute("error", "Đã xảy ra lỗi khi cập nhật mật khẩu.");
        }

        request.getRequestDispatcher("change_password.jsp").forward(request, response);
    }

}
