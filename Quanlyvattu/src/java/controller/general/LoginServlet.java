package controller.general;

import DAO.UsersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;

import java.io.IOException;

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
            Users user = UsersDAO.getUserByEmailAndPassword(email, password);

            if (user == null) {
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

            if ("on".equals(request.getParameter("remember"))) {
                Cookie cookie = new Cookie("rememberedEmail", email);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("rememberedEmail", "");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

//            String redirectUrl = switch (user.getRoleId()) {
//                case 1 ->
//                    "admin/dashboard.jsp";
//                case 2 ->
//                    "staff/dashboard.jsp";
//                case 3 ->
//                    "director/dashboard.jsp";
//                case 4 ->
//                    "company/home.jsp";
//                default ->
//                    null;
//            };

//            if (redirectUrl != null) {
//                response.sendRedirect(redirectUrl);
//            } else {
//                session.invalidate();
//                request.setAttribute("errorMsg", "Unrecognized user role. Please contact the administrator.");
//                request.getRequestDispatcher("login.jsp").forward(request, response);
//            }

            response.sendRedirect("homepage.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "System error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user login and role-based redirection.";
    }
}
