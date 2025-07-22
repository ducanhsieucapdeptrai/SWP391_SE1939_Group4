/**
 *
 * @author anhdu
 */
package controller.general;

import DAO.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Notification;
import model.Users;

@WebServlet("/all-notifications")
public class AllNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Users currentUser = (Users) request.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            NotificationDAO dao = new NotificationDAO(); // ✅ KHỞI TẠO ĐÚNG
            List<Notification> list = dao.getAllNotificationsForUser(currentUser.getUserId());
            request.setAttribute("notificationList", list);
            request.setAttribute("pageContent", "/all_notifications.jsp");  // ❗ Đúng tên JSP
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // ✅ In ra lỗi
            response.sendError(500, "Server error loading notifications");
        }
    }
}
