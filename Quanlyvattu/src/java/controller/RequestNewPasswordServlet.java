package controller;

import DAO.UsersDAO;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Role;
import model.Users;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.PasswordAuthentication;

@WebServlet("/request-new-password")
public class RequestNewPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roles = UsersDAO.getAllRoles();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("request-new-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String roleIdStr = request.getParameter("roleId");

        List<Role> roles = UsersDAO.getAllRoles();
        request.setAttribute("roles", roles);

        if (fullName == null || fullName.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || roleIdStr == null || roleIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Please fill in all required fields.");
            request.getRequestDispatcher("request-new-password.jsp").forward(request, response);
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid role.");
            request.getRequestDispatcher("request-new-password.jsp").forward(request, response);
            return;
        }

        Users user = UsersDAO.getUserByEmail(email);
        if (user == null || !user.getFullName().equalsIgnoreCase(fullName)
                || !user.getPhone().equals(phone) || user.getRoleId() != roleId) {
            request.setAttribute("error", "Incorrect user information.");
            request.getRequestDispatcher("request-new-password.jsp").forward(request, response);
            return;
        }

        String newPassword = generateRandomPassword();
        boolean updateSuccess = UsersDAO.updatePassword(email, newPassword);

        if (updateSuccess) {
            boolean mailSent = sendPasswordEmail(email, newPassword);
            if (mailSent) {
                request.setAttribute("message", "A new password has been sent to your email address.");
            } else {
                request.setAttribute("error", "Password updated but failed to send email. Please contact support.");
            }
        } else {
            request.setAttribute("error", "Failed to update password. Please try again later.");
        }

        request.getRequestDispatcher("request-new-password.jsp").forward(request, response);
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private boolean sendPasswordEmail(String recipientEmail, String newPassword) {
        final String fromEmail = "quanlyvattu4@gmail.com";
        final String emailPassword = "juuzmqxwigutmgcu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your New Password");
            message.setText("Dear user,\n\nYour new password is: " + newPassword
                    + "\n\nPlease log in and change it as soon as possible.\n\nRegards,\nMaterial Management Team");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles password reset requests by verifying user information, updating password, and sending the new password via email.";
    }
}
