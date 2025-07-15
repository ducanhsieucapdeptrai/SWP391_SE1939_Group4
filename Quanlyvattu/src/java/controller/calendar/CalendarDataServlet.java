package controller.calendar;

import DAO.EventDAO;
import com.google.gson.Gson;
import model.Event;
import model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "CalendarDataServlet", urlPatterns = {"/calendar-data"})
public class CalendarDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Users user = (Users) session.getAttribute("currentUser");
        String startStr = request.getParameter("start");
        String endStr = request.getParameter("end");

        // FullCalendar sends dates in ISO 8601 format (e.g., 2023-10-01T00:00:00Z)
        // We can parse them into Timestamp objects.
        Timestamp start = Timestamp.valueOf(startStr.replace("T", " ").substring(0, 19));
        Timestamp end = Timestamp.valueOf(endStr.replace("T", " ").substring(0, 19));

        EventDAO eventDAO = new EventDAO();
        List<Event> events = eventDAO.getEventsByUserId(user.getUserId(), start, end);

        // Manually build the JSON for FullCalendar compatibility
        java.util.List<java.util.Map<String, Object>> eventList = new java.util.ArrayList<>();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        for (Event event : events) {
            java.util.Map<String, Object> eventMap = new java.util.HashMap<>();
            eventMap.put("id", event.getEventId());
            eventMap.put("title", event.getTitle());
            eventMap.put("start", event.getStartTime().toLocalDateTime().format(formatter));
            if (event.getEndTime() != null) {
                eventMap.put("end", event.getEndTime().toLocalDateTime().format(formatter));
            }

            // Use extendedProps for custom data
            java.util.Map<String, String> extendedProps = new java.util.HashMap<>();
            extendedProps.put("description", event.getDescription());
            extendedProps.put("eventType", event.getEventType());
            eventMap.put("extendedProps", extendedProps);

            eventList.add(eventMap);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(eventList));
    }
}
