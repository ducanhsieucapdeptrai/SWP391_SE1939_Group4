package controller.admin;

import DAO.PasswordResetDAO;
import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Users;
import utils.HashUtil;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@WebServlet("/reset-pass-list")
public class ResetPassListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PasswordResetDAO dao = new PasswordResetDAO();
        List<model.PasswordResetRequest> requests = dao.getAllPendingRequests();
        request.setAttribute("resetRequests", requests);
        
        request.setAttribute("pageContent", "/View/WarehouseManager/reset-pass-list.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newPassword = request.getParameter("newPassword");
        String userIdStr = request.getParameter("userId");
        String requestIdStr = request.getParameter("requestId");

        if (newPassword == null || userIdStr == null || requestIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("alertType", "warning");
            request.setAttribute("alertMessage", "Password must be at least 6 characters.");
            doGet(request, response);
            return;
        }

        int userId = Integer.parseInt(userIdStr);
        int requestId = Integer.parseInt(requestIdStr);

        UserDAO userDAO = new UserDAO();
        PasswordResetDAO resetDAO = new PasswordResetDAO();

        Users user = userDAO.getUserById(userId);
        if (user == null) {
            request.setAttribute("alertType", "error");
            request.setAttribute("alertMessage", "User not found.");
            doGet(request, response);
            return;
        }

        // Hash password
        String hashed = HashUtil.hashPassword(newPassword);
        boolean updated = userDAO.updatePassword(user.getEmail(), hashed);

        if (!updated) {
            request.setAttribute("alertType", "error");
            request.setAttribute("alertMessage", "Failed to update password.");
            doGet(request, response);
            return;
        }

        // Mark request as processed
        boolean marked = resetDAO.markAsProcessed(requestId);
        if (!marked) {
            request.setAttribute("alertType", "error");
            request.setAttribute("alertMessage", "Failed to mark request as processed.");
            doGet(request, response);
            return;
        }

        // Send email to user
        boolean mailSent = sendNewPasswordToUser(user.getEmail(), newPassword);

        if (mailSent) {
            request.setAttribute("alertType", "success");
            request.setAttribute("alertMessage", "Password reset successfully. New password sent via email.");
        } else {
            request.setAttribute("alertType", "warning");
            request.setAttribute("alertMessage", "Password updated, but failed to send email.");
        }

        doGet(request, response);
    }

    private boolean sendNewPasswordToUser(String toEmail, String password) {
        final String fromEmail = "quanlyvattu4@gmail.com";
        final String emailPassword = "juuzmqxwigutmgcu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Warehouse Management"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your Password Has Been Reset");
            message.setText("Dear user,\n\nYour password has been reset by the administrator.\n\nNew Password: "
                    + password + "\n\nPlease log in and change your password immediately.\n\nRegards,\nWarehouse Management");

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
