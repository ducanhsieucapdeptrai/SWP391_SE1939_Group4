package controller.calendar;

import DAO.EventDAO;
import model.Event;
import model.Users;

// ===================================
// CORRECTIONS ARE HERE
// ===================================
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// ===================================

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "EventActionServlet", urlPatterns = {"/event-action"})
public class EventActionServlet extends HttpServlet { // This HttpServlet now correctly refers to jakarta.servlet.http.HttpServlet

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        

        Users user = (Users) session.getAttribute("currentUser");
        String action = request.getParameter("action");
        EventDAO eventDAO = new EventDAO();

        try {
            if ("add".equals(action)) {
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String startTimeStr = request.getParameter("startTime"); // Expected format: yyyy-MM-dd'T'HH:mm
                String eventType = request.getParameter("eventType");

                LocalDateTime localDateTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                Timestamp startTime = Timestamp.valueOf(localDateTime);

                Event event = new Event();
                event.setUserId(user.getUserId());
                event.setTitle(title);
                event.setDescription(description);
                event.setStartTime(startTime);

                String endTimeStr = request.getParameter("endTime");
                Timestamp endTime = null;
                if (endTimeStr != null && !endTimeStr.isEmpty()) {
                    LocalDateTime endLocalDateTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    endTime = Timestamp.valueOf(endLocalDateTime);
                } else {
                    endTime = startTime; // Default to start time if not provided
                }
                event.setEndTime(endTime);
                event.setEventType(eventType);

                eventDAO.addEvent(event);
                response.sendRedirect("dashboard");

            } else if ("update".equals(action)) {
                // Logic for updating an event
            } else if ("delete".equals(action)) {
                // Logic for deleting an event
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to an error page or send an error response
            response.sendRedirect("dashboard?error=true");
        }
    }
}