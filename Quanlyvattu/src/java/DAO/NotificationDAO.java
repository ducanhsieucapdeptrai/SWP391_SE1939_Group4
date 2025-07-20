package DAO;

import dal.DBContext;
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends DBContext {

    // Enhanced method to insert notification with all new fields
    public void insertNotification(Integer userId, int typeId, String title, String message, String url, 
                                 Integer relatedId, String relatedType, String priority) throws SQLException {
        String sql = "INSERT INTO Notifications (UserId, TypeId, Title, Message, Url, RelatedId, RelatedType, Priority) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (userId != null) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, typeId);
            ps.setString(3, title);
            ps.setString(4, message);
            ps.setString(5, url);
            if (relatedId != null) {
                ps.setInt(6, relatedId);
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setString(7, relatedType);
            ps.setString(8, priority != null ? priority : "MEDIUM");
            ps.executeUpdate();
        }
    }

    // Backward compatibility method
    public void insertNotification(Integer userId, String message, String url) throws SQLException {
        insertNotification(userId, 12, "Thông báo hệ thống", message, url, null, "SYSTEM", "MEDIUM");
    }

    // Get all notifications for a user (read and unread)
    public List<Notification> getAllNotifications(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT n.*, nt.TypeName FROM Notifications n " +
                    "LEFT JOIN NotificationTypes nt ON n.TypeId = nt.TypeId " +
                    "WHERE n.UserId IS NULL OR n.UserId = ? " +
                    "ORDER BY n.CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = mapResultSetToNotification(rs);
                list.add(n);
            }
        }
        return list;
    }

    // Get only unread notifications
    public List<Notification> getUnreadNotifications(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT n.*, nt.TypeName FROM Notifications n " +
                    "LEFT JOIN NotificationTypes nt ON n.TypeId = nt.TypeId " +
                    "WHERE (n.UserId IS NULL OR n.UserId = ?) AND n.IsRead = FALSE " +
                    "ORDER BY n.CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = mapResultSetToNotification(rs);
                list.add(n);
            }
        }
        return list;
    }

    // Mark notification as read
    public void markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE Notifications SET IsRead = TRUE WHERE NotificationId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        }
    }

    // Mark all notifications as read for a user
    public void markAllAsRead(int userId) throws SQLException {
        String sql = "UPDATE Notifications SET IsRead = TRUE WHERE UserId = ? OR UserId IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    // Get notification count for a user
    public int getUnreadCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notifications WHERE (UserId = ? OR UserId IS NULL) AND IsRead = FALSE";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Helper method to map ResultSet to Notification object
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setNotificationId(rs.getInt("NotificationId"));
        n.setUserId((Integer) rs.getObject("UserId"));
        n.setTypeId(rs.getInt("TypeId"));
        n.setTypeName(rs.getString("TypeName"));
        n.setTitle(rs.getString("Title"));
        n.setMessage(rs.getString("Message"));
        n.setUrl(rs.getString("Url"));
        n.setRelatedId((Integer) rs.getObject("RelatedId"));
        n.setRelatedType(rs.getString("RelatedType"));
        n.setIsRead(rs.getBoolean("IsRead"));
        n.setPriority(rs.getString("Priority"));
        n.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return n;
    }

    // Specific notification creation methods for different events
    
    // Category/Subcategory notifications
    public void createCategoryNotification(String categoryName, boolean isSubCategory) throws SQLException {
        int typeId = isSubCategory ? 2 : 1; // SUBCATEGORY_ADDED : CATEGORY_ADDED
        String title = isSubCategory ? "Danh mục con mới" : "Danh mục mới";
        String message = isSubCategory ? 
            "Danh mục con '" + categoryName + "' đã được thêm vào hệ thống." :
            "Danh mục '" + categoryName + "' đã được thêm vào hệ thống.";
        insertNotification(null, typeId, title, message, "catalog", null, "CATEGORY", "LOW");
    }

    // Material notification
    public void createMaterialNotification(int materialId, String materialName) throws SQLException {
        String title = "Vật tư mới";
        String message = "Vật tư '" + materialName + "' đã được thêm vào hệ thống.";
        insertNotification(null, 3, title, message, "materials", materialId, "MATERIAL", "LOW");
    }

    // Request notifications
    public void createRequestNotification(int requestId, int requestedBy, String requestType) throws SQLException {
        String title = "Yêu cầu mới";
        String message = "Bạn có yêu cầu " + requestType + " mới đang chờ xử lý.";
        insertNotification(requestedBy, 4, title, message, "my-request", requestId, "REQUEST", "MEDIUM");
    }

    public void createRequestApprovalNotification(int requestId, int requestedBy, boolean approved, String note) throws SQLException {
        int typeId = approved ? 5 : 6; // REQUEST_APPROVED : REQUEST_REJECTED
        String title = approved ? "Yêu cầu được duyệt" : "Yêu cầu bị từ chối";
        String message = approved ? 
            "Yêu cầu của bạn đã được duyệt. Giờ bạn có thể tạo đơn hàng." + (note != null ? " Ghi chú: " + note : "") :
            "Yêu cầu của bạn đã bị từ chối." + (note != null ? " Lý do: " + note : "");
        String url = approved ? "create-order?requestId=" + requestId : "my-request";
        insertNotification(requestedBy, typeId, title, message, url, requestId, "REQUEST", "HIGH");
    }

    // Order notifications
    public void createOrderNotification(int orderId, int createdBy, String orderType) throws SQLException {
        String title = "Đơn hàng mới";
        String message = "Đơn " + orderType + " mới đã được tạo và đang chờ duyệt.";
        insertNotification(createdBy, 7, title, message, "my-orders", orderId, "ORDER", "MEDIUM");
    }

    public void createOrderApprovalNotification(int orderId, int createdBy, boolean approved, String note) throws SQLException {
        int typeId = approved ? 8 : 9; // ORDER_APPROVED : ORDER_REJECTED
        String title = approved ? "Đơn hàng được duyệt" : "Đơn hàng bị từ chối";
        String message = approved ? 
            "Đơn hàng của bạn đã được duyệt và sẽ được xử lý." + (note != null ? " Ghi chú: " + note : "") :
            "Đơn hàng của bạn đã bị từ chối." + (note != null ? " Lý do: " + note : "");
        insertNotification(createdBy, typeId, title, message, "my-orders", orderId, "ORDER", "HIGH");
    }

    // Task notifications
    public void createTaskCompletionNotification(int taskId, int staffId, String taskDescription) throws SQLException {
        String title = "Nhiệm vụ hoàn thành";
        String message = "Nhiệm vụ '" + taskDescription + "' đã được hoàn thành.";
        insertNotification(staffId, 10, title, message, "tasks", taskId, "TASK", "MEDIUM");
    }

    public void createTaskStatusChangeNotification(int taskId, int staffId, String taskDescription) throws SQLException {
        String title = "Nhiệm vụ đang thực hiện";
        String message = "Nhiệm vụ '" + taskDescription + "' đã chuyển từ sắp tới sang đang thực hiện.";
        insertNotification(staffId, 11, title, message, "tasks", taskId, "TASK", "MEDIUM");
    }
}


