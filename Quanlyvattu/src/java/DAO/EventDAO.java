package DAO;

import dal.DBContext;
import model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO extends DBContext {

    public List<Event> getEventsByUserId(int userId, Timestamp start, Timestamp end) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Events WHERE UserId = ? AND StartTime < ? AND EndTime > ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setTimestamp(2, end);
            ps.setTimestamp(3, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Event event = new Event();
                    event.setEventId(rs.getInt("EventId"));
                    event.setUserId(rs.getInt("UserId"));
                    event.setTitle(rs.getString("Title"));
                    event.setDescription(rs.getString("Description"));
                    event.setStartTime(rs.getTimestamp("StartTime"));
                    event.setEndTime(rs.getTimestamp("EndTime"));
                    event.setEventType(rs.getString("EventType"));
                    events.add(event);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void addEvent(Event event) {
        String sql = "INSERT INTO Events (UserId, Title, Description, StartTime, EndTime, EventType) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, event.getUserId());
            ps.setString(2, event.getTitle());
            ps.setString(3, event.getDescription());
            ps.setTimestamp(4, event.getStartTime());
            ps.setTimestamp(5, event.getEndTime());
            ps.setString(6, event.getEventType());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEvent(Event event) {
        String sql = "UPDATE Events SET Title = ?, Description = ?, StartTime = ?, EndTime = ?, EventType = ? WHERE EventId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setTimestamp(3, event.getStartTime());
            ps.setTimestamp(4, event.getEndTime());
            ps.setString(5, event.getEventType());
            ps.setInt(6, event.getEventId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(int eventId) {
        String sql = "DELETE FROM Events WHERE EventId = ?";
        try (Connection conn = getNewConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
