package controller.general;

import DAO.NotificationDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Notification;
import model.Users; // ✅ Sửa đúng tên class Users

import java.io.IOException;
import java.util.List;

@WebServlet("/get-notifications")
public class GetNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

// Sửa lại đúng theo session đã set
        Users user = (Users) request.getSession().getAttribute("currentUser");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            NotificationDAO dao = new NotificationDAO();
            List<Notification> list = dao.getUnreadNotifications(user.getUserId());

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            new Gson().toJson(list, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}
