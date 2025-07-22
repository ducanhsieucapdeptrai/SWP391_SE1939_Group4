package controller.admin;

import DAO.RoleDAO;
import DAO.UserDAO;
import Helper.ImageHelper;
import Helper.AuthorizationHelper;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Role;
import model.Users;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import utils.HashUtil;

@WebServlet("/add-user")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class AddUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasRole(request, "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        RoleDAO roleDAO = new RoleDAO();
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("roleList", roles);
        request.setAttribute("pageContent", "/View/WarehouseManager/add-user.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasRole(request, "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        boolean isActive = "1".equals(request.getParameter("status"));

        if (!fullName.matches("^[A-Za-z\\s]{1,20}$")) {
            request.setAttribute("errorMessage", "Full name must be alphabetic only and max 20 characters.");
            doGet(request, response);
            return;
        }

        if (!phone.matches("\\d+")) {
            request.setAttribute("errorMessage", "Phone number must contain only digits.");
            doGet(request, response);
            return;
        }

        UserDAO dao = new UserDAO();
        Users user = dao.getUserByEmail(email);
        String storedPassword = user.getPassword();
        String inputPassword = password;
        if (inputPassword.length() < 6) {
//            request.setAttribute("errorMessage", "Password must be at least 6 characters.");
//            doGet(request, response);
//            return;
            String hashedInput = HashUtil.hashPassword(inputPassword);
            if (hashedInput.equals(storedPassword)) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters.");
            doGet(request, response);
            return;
            }
        }

        if (dao.getUserByEmail(email) != null) {
            request.setAttribute("errorMessage", "Email already exists.");
            doGet(request, response);
            return;
        }

        if (dao.getUserByPhone(phone) != null) {
            request.setAttribute("errorMessage", "Phone number already exists.");
            doGet(request, response);
            return;
        }

        String fileName = null;
        try {
            Part avatarPart = request.getPart("avatar");
            String imgName = email.split("@")[0] + System.currentTimeMillis();
            ImageHelper imgHelper = new ImageHelper(this);
            fileName = imgHelper.processImageUpload(avatarPart, imgName);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to upload avatar.");
            doGet(request, response);
            return;
        }

//        Users user = new Users();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setIsActive(isActive);
        user.setRoleId(roleId);
        user.setUserImage(fileName != null ? fileName : "");

        boolean inserted = dao.addUser(user);
        if (!inserted) {
            request.setAttribute("errorMessage", "Failed to add user. Please try again.");
            doGet(request, response);
            return;
        }

        try {
            sendWelcomeEmail(email, fullName, password);
        } catch (Exception e) {
            System.out.println("⚠ Email send failed: " + e.getMessage());
        }

        request.setAttribute("successMessage", "User added and password sent via email!");
        doGet(request, response);
    }

    private void sendWelcomeEmail(String toEmail, String fullName, String password) throws UnsupportedEncodingException {
        final String fromEmail = "quanlyvattu4@gmail.com";
        final String emailPassword = "juuzmqxwigutmgcu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Warehouse System"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your Account Information");
            message.setText("Dear " + fullName + ",\n\n"
                    + "An account has been created for you:\n"
                    + "Email: " + toEmail + "\n"
                    + "Password: " + password + "\n\n"
                    + "Please login and change your password.\n\nWarehouse Team");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Email send error", e);
        }
    }
}
