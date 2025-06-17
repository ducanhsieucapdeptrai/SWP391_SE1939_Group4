package controller.general; // Đổi thành package thực tế bạn dùng

import DAO.NotificationDAO;
import com.google.gson.Gson;
import model.Notification;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {
    private NotificationDAO notificationDAO;

    @Override
    public void init() throws ServletException {
        notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Giả sử UserId đang lưu trong session
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"error\": \"User not logged in\"}");
            return;
        }

        try {
            List<Notification> notifications = notificationDAO.getNotificationsByUserId(userId);
            Gson gson = new Gson();
            String json = gson.toJson(notifications);
            out.print(json);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Could not fetch notifications\"}");
        }
    }
}
