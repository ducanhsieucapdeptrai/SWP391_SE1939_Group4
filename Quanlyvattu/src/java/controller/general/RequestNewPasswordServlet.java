package controller.general;

import DAO.PasswordResetDAO;
import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;

import java.io.IOException;

@WebServlet("/request-new-password")
public class RequestNewPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/View/General/request-new-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        if (email == null || email.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both email and phone number.");
            request.getRequestDispatcher("/View/General/request-new-password.jsp").forward(request, response);
            return;
        }

        UserDAO dao = new UserDAO();
        Users user = dao.getUserByEmailAndPhone(email.trim(), phone.trim());

        if (user == null) {
            request.setAttribute("error", "Incorrect email or phone number.");
            request.getRequestDispatcher("/View/General/request-new-password.jsp").forward(request, response);
            return;
        }

        PasswordResetDAO resetDAO = new PasswordResetDAO();
        boolean inserted = resetDAO.insertPasswordResetRequest(user.getUserId());

        if (inserted) {
            request.setAttribute("message", "Your password reset request has been submitted. Please wait for admin approval.");
        } else {
            request.setAttribute("error", "Failed to submit request. You may have already requested recently.");
        }

        request.getRequestDispatcher("/View/General/request-new-password.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles password reset requests by submitting them to admin for approval.";
    }
}
