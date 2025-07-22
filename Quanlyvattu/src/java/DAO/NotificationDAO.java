package DAO;

import dal.DBContext;
import model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class NotificationDAO extends DBContext {

    public void insertNotification(Integer userId, int typeId, String title, String message, String url,
            Integer relatedId, String relatedType, String priority) throws SQLException {
        String sql = "INSERT INTO Notifications (UserId, TypeId, Title, Message, Url, RelatedId, RelatedType, Priority) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

    public void insertNotification(Integer userId, String message, String url) throws SQLException {
        insertNotification(userId, 12, "Thông báo hệ thống", message, url, null, "SYSTEM", "MEDIUM");
    }

    public List<Notification> getAllNotifications(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT n.*, nt.TypeName AS NotificationTypeName FROM Notifications n "
                + "LEFT JOIN NotificationTypes nt ON n.TypeId = nt.TypeId "
                + "WHERE n.UserId IS NULL OR n.UserId = ? ORDER BY n.CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNotification(rs));
            }
        }
        return list;
    }

    public List<Notification> getUnreadNotifications(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT n.*, nt.TypeName AS NotificationTypeName FROM Notifications n "
                + "LEFT JOIN NotificationTypes nt ON n.TypeId = nt.TypeId "
                + "WHERE (n.UserId IS NULL OR n.UserId = ?) AND n.IsRead = FALSE "
                + "ORDER BY n.CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNotification(rs));
            }
        }
        return list;
    }

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

    public void markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE Notifications SET IsRead = TRUE WHERE NotificationId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        }
    }

    public void markAllAsRead(int userId) throws SQLException {
        String sql = "UPDATE Notifications SET IsRead = TRUE WHERE UserId = ? OR UserId IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public List<Notification> getAllNotificationsForUser(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT n.*, nt.TypeName AS NotificationTypeName FROM Notifications n "
                + "LEFT JOIN NotificationTypes nt ON n.TypeId = nt.TypeId "
                + "WHERE n.UserId = ? OR n.UserId IS NULL ORDER BY n.CreatedAt DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToNotification(rs));
            }
        }
        return list;
    }

    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification n = new Notification();

        n.setNotificationId(rs.getInt("NotificationId"));

        Object userIdObj = rs.getObject("UserId");
        if (userIdObj != null) {
            n.setUserId((Integer) userIdObj);
        } else {
            n.setUserId(null);
        }

        n.setTypeId(rs.getInt("TypeId"));
        n.setTypeName(rs.getString("NotificationTypeName"));
        n.setTitle(rs.getString("Title"));
        n.setMessage(rs.getString("Message"));
        n.setUrl(rs.getString("Url"));

        Object relatedIdObj = rs.getObject("RelatedId");
        if (relatedIdObj != null) {
            n.setRelatedId((Integer) relatedIdObj);
        } else {
            n.setRelatedId(null);
        }

        n.setRelatedType(rs.getString("RelatedType"));
        n.setIsRead(rs.getBoolean("IsRead"));
        n.setPriority(rs.getString("Priority"));

        Timestamp timestamp = rs.getTimestamp("CreatedAt");
        if (timestamp != null) {
            String formatted = timestamp.toLocalDateTime()
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            n.setCreatedAt(formatted);
        } else {
            n.setCreatedAt("Không rõ thời gian");
        }

        return n;
    }

    // --- Các hàm tạo thông báo cụ thể ---
    public void createCategoryNotification(String categoryName, boolean isSubCategory) throws SQLException {
        int typeId = isSubCategory ? 2 : 1;
        String title = isSubCategory ? "Danh mục con mới" : "Danh mục mới";
        String message = (isSubCategory ? "Danh mục con '" : "Danh mục '") + categoryName + "' đã được thêm vào hệ thống.";
        insertNotification(null, typeId, title, message, "catalog", null, "CATEGORY", "LOW");
    }

    public void createMaterialNotification(int materialId, String materialName) throws SQLException {
        insertNotification(null, 3, "Vật tư mới",
                "Vật tư '" + materialName + "' đã được thêm vào hệ thống.",
                "materials", materialId, "MATERIAL", "LOW");
    }

    public void createRequestNotification(int requestId, int requestedBy, String requestType) throws SQLException {
        insertNotification(requestedBy, 4, "Yêu cầu mới",
                "Bạn có yêu cầu " + requestType + " mới đang chờ xử lý.",
                "my-request", requestId, "REQUEST", "MEDIUM");
    }

    public void createRequestApprovalNotification(int requestId, int requestedBy, boolean approved, String note) throws SQLException {
        int typeId = approved ? 5 : 6;
        String title = approved ? "Yêu cầu được duyệt" : "Yêu cầu bị từ chối";
        String message = approved
                ? "Yêu cầu của bạn đã được duyệt. Giờ bạn có thể tạo đơn hàng." + (note != null ? " Ghi chú: " + note : "")
                : "Yêu cầu của bạn đã bị từ chối." + (note != null ? " Lý do: " + note : "");
        String url = approved ? "create-order?requestId=" + requestId : "my-request";
        insertNotification(requestedBy, typeId, title, message, url, requestId, "REQUEST", "HIGH");
    }

    public void createOrderNotification(int orderId, int createdBy, String orderType) throws SQLException {
        insertNotification(createdBy, 7, "Đơn hàng mới",
                "Đơn " + orderType + " mới đã được tạo và đang chờ duyệt.",
                "my-orders", orderId, "ORDER", "MEDIUM");
    }

    public void createOrderApprovalNotification(int orderId, int createdBy, boolean approved, String note) throws SQLException {
        int typeId = approved ? 8 : 9;
        String title = approved ? "Đơn hàng được duyệt" : "Đơn hàng bị từ chối";
        String message = approved
                ? "Đơn hàng của bạn đã được duyệt và sẽ được xử lý." + (note != null ? " Ghi chú: " + note : "")
                : "Đơn hàng của bạn đã bị từ chối." + (note != null ? " Lý do: " + note : "");
        insertNotification(createdBy, typeId, title, message, "my-orders", orderId, "ORDER", "HIGH");
    }

    public void createTaskCompletionNotification(int taskId, int staffId, String taskDescription) throws SQLException {
        insertNotification(staffId, 10, "Nhiệm vụ hoàn thành",
                "Nhiệm vụ '" + taskDescription + "' đã được hoàn thành.",
                "tasks", taskId, "TASK", "MEDIUM");
    }

    public void createTaskStatusChangeNotification(int taskId, int staffId, String taskDescription) throws SQLException {
        insertNotification(staffId, 11, "Nhiệm vụ đang thực hiện",
                "Nhiệm vụ '" + taskDescription + "' đã chuyển từ sắp tới sang đang thực hiện.",
                "tasks", taskId, "TASK", "MEDIUM");
    }

    // Gửi noti hệ thống: user A vừa tạo yêu cầu loại XYZ
    public void createSystemRequestNotification(String fullName, String requestTypeName) throws SQLException {
        String sql = "INSERT INTO Notifications (UserId, TypeId, Title, Message, Url, RelatedType, Priority, IsRead, CreatedAt) "
                + "VALUES (NULL, 4, ?, ?, ?, ?, 'MEDIUM', FALSE, GETDATE())";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Yêu cầu mới");
            ps.setString(2, fullName + " đã tạo yêu cầu loại \"" + requestTypeName + "\".");
            ps.setString(3, "request-list");
            ps.setString(4, "REQUEST");
            ps.executeUpdate();
        }
    }

}
