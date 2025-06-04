package controller.general;

import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;

import java.io.IOException;
import utils.HashUtil;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMsg", "Please enter both email and password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            Users user = dao.getUserByEmail(email);

            if (user == null) {
                request.setAttribute("errorMsg", "Incorrect email or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            String storedPassword = user.getPassword();
            String inputPassword = password;

            boolean isPasswordCorrect = false;

            // Nếu password trong DB đã được hash
            if (storedPassword.length() == 64 && storedPassword.matches("[0-9a-fA-F]+")) {
                String hashedInput = HashUtil.hashPassword(inputPassword);
                if (hashedInput.equals(storedPassword)) {
                    isPasswordCorrect = true;
                }
            } else {
                // Nếu DB vẫn đang lưu mật khẩu plain text
                if (inputPassword.equals(storedPassword)) {
                    isPasswordCorrect = true;
                }
            }

            if (!isPasswordCorrect) {
                request.setAttribute("errorMsg", "Incorrect email or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            if (!user.isIsActive()) {
                request.setAttribute("errorMsg", "Your account is deactivated. Please contact the administrator.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userRole", user.getRole() != null ? user.getRole().getRoleName() : "Unknown");
            session.setAttribute("userImage", user.getUserImage());

            if ("on".equals(request.getParameter("remember"))) {
                Cookie cookie = new Cookie("rememberedEmail", email);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("rememberedEmail", "");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

            response.sendRedirect("dashboard");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "System error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        
        
    }

    @Override
    public String getServletInfo() {
        return "Handles user login and redirects to dashboard.";
    }
}
